package com.process.clash.application.realtime.service;

import com.process.clash.application.realtime.data.UserActivityStatus;
import com.process.clash.application.realtime.port.out.NotifyPresenceStatusChangedPort;
import com.process.clash.application.realtime.port.out.PresenceReconnectStatePort;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserPresenceServiceTest {

    private static final Instant BASE_TIME = Instant.parse("2026-03-19T00:00:00Z");

    @Mock
    private NotifyPresenceStatusChangedPort notifyPresenceStatusChangedPort;

    private MutableClock clock;
    private InMemoryPresenceReconnectStatePort presenceReconnectStatePort;
    private UserPresenceService userPresenceService;

    @BeforeEach
    void setUp() {
        clock = new MutableClock(BASE_TIME);
        presenceReconnectStatePort = new InMemoryPresenceReconnectStatePort();
        userPresenceService = new UserPresenceService(
            List.of(notifyPresenceStatusChangedPort),
            presenceReconnectStatePort,
            clock,
            Duration.ofMinutes(1)
        );
    }

    @Test
    @DisplayName("연결, 자리비움, 복귀, 연결해제, timeout 흐름에 따라 상태를 갱신한다")
    void presenceStatusTransitions() {
        userPresenceService.connected("conn-1", 1L);
        assertThat(userPresenceService.getStatus(1L)).isEqualTo(UserActivityStatus.ONLINE);
        verify(notifyPresenceStatusChangedPort).notifyStatusChanged(
            1L,
            UserActivityStatus.OFFLINE,
            UserActivityStatus.ONLINE
        );

        userPresenceService.markedAway("conn-1");
        assertThat(userPresenceService.getStatus(1L)).isEqualTo(UserActivityStatus.AWAY);
        verify(notifyPresenceStatusChangedPort).notifyStatusChanged(
            1L,
            UserActivityStatus.ONLINE,
            UserActivityStatus.AWAY
        );

        userPresenceService.markedOnline("conn-1");
        assertThat(userPresenceService.getStatus(1L)).isEqualTo(UserActivityStatus.ONLINE);
        verify(notifyPresenceStatusChangedPort).notifyStatusChanged(
            1L,
            UserActivityStatus.AWAY,
            UserActivityStatus.ONLINE
        );

        userPresenceService.disconnected("conn-1");
        assertThat(userPresenceService.getStatus(1L)).isEqualTo(UserActivityStatus.RECONNECTING);
        verify(notifyPresenceStatusChangedPort).notifyStatusChanged(
            1L,
            UserActivityStatus.ONLINE,
            UserActivityStatus.RECONNECTING
        );

        clock.plusSeconds(61);
        userPresenceService.expireReconnectingUsers();
        assertThat(userPresenceService.getStatus(1L)).isEqualTo(UserActivityStatus.OFFLINE);
        verify(notifyPresenceStatusChangedPort).notifyStatusChanged(
            1L,
            UserActivityStatus.RECONNECTING,
            UserActivityStatus.OFFLINE
        );
    }

    @Test
    @DisplayName("다중 연결에서는 하나라도 active면 ONLINE, 모두 away면 AWAY, 모두 끊기면 RECONNECTING이다")
    void multiConnectionStatusRule() {
        userPresenceService.connected("conn-1", 1L);
        userPresenceService.connected("conn-2", 1L);

        userPresenceService.markedAway("conn-1");
        assertThat(userPresenceService.getStatus(1L)).isEqualTo(UserActivityStatus.ONLINE);

        userPresenceService.markedAway("conn-2");
        assertThat(userPresenceService.getStatus(1L)).isEqualTo(UserActivityStatus.AWAY);

        userPresenceService.disconnected("conn-2");
        assertThat(userPresenceService.getStatus(1L)).isEqualTo(UserActivityStatus.AWAY);

        userPresenceService.disconnected("conn-1");
        assertThat(userPresenceService.getStatus(1L)).isEqualTo(UserActivityStatus.RECONNECTING);
    }

    @Test
    @DisplayName("여러 사용자 상태를 일괄 조회한다")
    void getStatuses_returnsStatusesByUserId() {
        userPresenceService.connected("u1-1", 1L);
        userPresenceService.connected("u2-1", 2L);
        userPresenceService.markedAway("u2-1");
        userPresenceService.connected("u3-1", 3L);
        userPresenceService.disconnected("u3-1");

        Map<Long, UserActivityStatus> statuses = userPresenceService.getStatuses(
            java.util.List.of(1L, 2L, 3L, 4L)
        );

        assertThat(statuses.get(1L)).isEqualTo(UserActivityStatus.ONLINE);
        assertThat(statuses.get(2L)).isEqualTo(UserActivityStatus.AWAY);
        assertThat(statuses.get(3L)).isEqualTo(UserActivityStatus.RECONNECTING);
        assertThat(statuses.get(4L)).isEqualTo(UserActivityStatus.OFFLINE);
    }

    @Test
    @DisplayName("단건 상태 조회는 reconnect 조회 중 connect와 원자적으로 스냅샷을 읽는다")
    void getStatus_readsAtomicallyWhileReconnectLookupIsInFlight() throws Exception {
        BlockingPresenceReconnectStatePort blockingPort = new BlockingPresenceReconnectStatePort(Set.of(1L));
        UserPresenceService service = new UserPresenceService(
            List.of(notifyPresenceStatusChangedPort),
            blockingPort,
            clock,
            Duration.ofMinutes(1)
        );

        FutureTask<UserActivityStatus> statusTask = new FutureTask<>(() -> service.getStatus(1L));
        Thread statusThread = new Thread(statusTask, "presence-get-status");
        statusThread.start();

        assertThat(blockingPort.awaitSingleLookupStarted()).isTrue();

        CountDownLatch connectStarted = new CountDownLatch(1);
        FutureTask<Void> connectTask = new FutureTask<>(() -> {
            connectStarted.countDown();
            service.connected("conn-1", 1L);
            return null;
        });
        Thread connectThread = new Thread(connectTask, "presence-connect");
        connectThread.start();

        assertThat(connectStarted.await(1, TimeUnit.SECONDS)).isTrue();
        assertThat(blockingPort.awaitClearCalled(500)).isFalse();

        blockingPort.releaseLookup();

        assertThat(statusTask.get(1, TimeUnit.SECONDS)).isEqualTo(UserActivityStatus.RECONNECTING);
        connectTask.get(1, TimeUnit.SECONDS);
        assertThat(service.getStatus(1L)).isEqualTo(UserActivityStatus.ONLINE);
    }

    @Test
    @DisplayName("배치 상태 조회는 reconnect 조회 중 connect와 원자적으로 스냅샷을 읽는다")
    void getStatuses_readsAtomicallyWhileReconnectLookupIsInFlight() throws Exception {
        BlockingPresenceReconnectStatePort blockingPort = new BlockingPresenceReconnectStatePort(Set.of(1L));
        UserPresenceService service = new UserPresenceService(
            List.of(notifyPresenceStatusChangedPort),
            blockingPort,
            clock,
            Duration.ofMinutes(1)
        );

        FutureTask<Map<Long, UserActivityStatus>> statusesTask = new FutureTask<>(
            () -> service.getStatuses(List.of(1L, 2L))
        );
        Thread statusesThread = new Thread(statusesTask, "presence-get-statuses");
        statusesThread.start();

        assertThat(blockingPort.awaitBatchLookupStarted()).isTrue();

        CountDownLatch connectStarted = new CountDownLatch(1);
        FutureTask<Void> connectTask = new FutureTask<>(() -> {
            connectStarted.countDown();
            service.connected("conn-1", 1L);
            return null;
        });
        Thread connectThread = new Thread(connectTask, "presence-batch-connect");
        connectThread.start();

        assertThat(connectStarted.await(1, TimeUnit.SECONDS)).isTrue();
        assertThat(blockingPort.awaitClearCalled(500)).isFalse();

        blockingPort.releaseLookup();

        Map<Long, UserActivityStatus> statuses = statusesTask.get(1, TimeUnit.SECONDS);
        assertThat(statuses.get(1L)).isEqualTo(UserActivityStatus.RECONNECTING);
        assertThat(statuses.get(2L)).isEqualTo(UserActivityStatus.OFFLINE);
        connectTask.get(1, TimeUnit.SECONDS);
        assertThat(service.getStatus(1L)).isEqualTo(UserActivityStatus.ONLINE);
    }

    @Test
    @DisplayName("유예 시간 내 재연결하면 RECONNECTING 상태에서 ONLINE으로 복귀한다")
    void reconnectWithinGrace_restoresOnline() {
        userPresenceService.connected("conn-1", 1L);
        userPresenceService.disconnected("conn-1");

        assertThat(userPresenceService.getStatus(1L)).isEqualTo(UserActivityStatus.RECONNECTING);

        clock.plusSeconds(30);
        userPresenceService.connected("conn-2", 1L);

        assertThat(userPresenceService.getStatus(1L)).isEqualTo(UserActivityStatus.ONLINE);
        verify(notifyPresenceStatusChangedPort).notifyStatusChanged(
            1L,
            UserActivityStatus.RECONNECTING,
            UserActivityStatus.ONLINE
        );
    }

    @Test
    @DisplayName("상태가 바뀌지 않으면 알림을 보내지 않는다")
    void doesNotNotifyWhenStatusUnchanged() {
        userPresenceService.connected("conn-1", 1L);
        userPresenceService.markedOnline("conn-1");

        verify(notifyPresenceStatusChangedPort, never()).notifyStatusChanged(
            1L,
            UserActivityStatus.ONLINE,
            UserActivityStatus.ONLINE
        );
    }

    @Test
    @DisplayName("일부 notifier에서 예외가 발생해도 다른 notifier 알림은 계속된다")
    void continuesDispatchWhenNotifierThrows() {
        NotifyPresenceStatusChangedPort failingNotifier = mock(NotifyPresenceStatusChangedPort.class);
        NotifyPresenceStatusChangedPort succeedingNotifier = mock(NotifyPresenceStatusChangedPort.class);
        UserPresenceService service = new UserPresenceService(
            List.of(failingNotifier, succeedingNotifier),
            new InMemoryPresenceReconnectStatePort(),
            clock,
            Duration.ofMinutes(1)
        );

        doThrow(new RuntimeException("notify failed")).when(failingNotifier).notifyStatusChanged(
            1L,
            UserActivityStatus.OFFLINE,
            UserActivityStatus.ONLINE
        );

        service.connected("conn-1", 1L);

        verify(failingNotifier).notifyStatusChanged(
            1L,
            UserActivityStatus.OFFLINE,
            UserActivityStatus.ONLINE
        );
        verify(succeedingNotifier).notifyStatusChanged(
            1L,
            UserActivityStatus.OFFLINE,
            UserActivityStatus.ONLINE
        );
    }

    @Test
    @DisplayName("startup recovery 시 오프라인 사용자만 RECONNECTING으로 등록한다")
    void registerReconnectingUsers_marksOnlyOfflineUsers() {
        userPresenceService.connected("conn-1", 1L);

        int recoveredCount = userPresenceService.registerReconnectingUsers(java.util.Arrays.asList(1L, 2L, 3L, null));

        assertThat(recoveredCount).isEqualTo(2);
        assertThat(userPresenceService.getStatus(1L)).isEqualTo(UserActivityStatus.ONLINE);
        assertThat(userPresenceService.getStatus(2L)).isEqualTo(UserActivityStatus.RECONNECTING);
        assertThat(userPresenceService.getStatus(3L)).isEqualTo(UserActivityStatus.RECONNECTING);
    }

    @Test
    @DisplayName("reconnect 저장소 예외가 발생해도 disconnect는 OFFLINE 전이를 전파한다")
    void disconnect_whenReconnectStoreFails_fallsBackToOfflineTransition() {
        PresenceReconnectStatePort failingPort = mock(PresenceReconnectStatePort.class);
        when(failingPort.isReconnectPending(1L)).thenThrow(new RuntimeException("redis down"));
        doThrow(new RuntimeException("redis down")).when(failingPort).clearReconnectPending(1L);
        doThrow(new RuntimeException("redis down")).when(failingPort).markReconnectPending(1L, BASE_TIME.plusSeconds(60));

        UserPresenceService service = new UserPresenceService(
            List.of(notifyPresenceStatusChangedPort),
            failingPort,
            clock,
            Duration.ofMinutes(1)
        );

        assertThatNoException().isThrownBy(() -> service.connected("conn-1", 1L));
        assertThatNoException().isThrownBy(() -> service.disconnected("conn-1"));

        assertThat(service.getStatus(1L)).isEqualTo(UserActivityStatus.OFFLINE);
        verify(notifyPresenceStatusChangedPort).notifyStatusChanged(
            1L,
            UserActivityStatus.ONLINE,
            UserActivityStatus.OFFLINE
        );
    }

    @Test
    @DisplayName("여러 사용자 상태 조회는 reconnect 상태를 배치 조회한다")
    void getStatuses_batchesReconnectLookup() {
        TrackingPresenceReconnectStatePort trackingPort = new TrackingPresenceReconnectStatePort(Set.of(2L));
        UserPresenceService service = new UserPresenceService(
            List.of(notifyPresenceStatusChangedPort),
            trackingPort,
            clock,
            Duration.ofMinutes(1)
        );

        service.connected("u1-1", 1L);
        trackingPort.resetCounters();

        Map<Long, UserActivityStatus> statuses = service.getStatuses(List.of(1L, 2L, 3L));

        assertThat(statuses.get(1L)).isEqualTo(UserActivityStatus.ONLINE);
        assertThat(statuses.get(2L)).isEqualTo(UserActivityStatus.RECONNECTING);
        assertThat(statuses.get(3L)).isEqualTo(UserActivityStatus.OFFLINE);
        assertThat(trackingPort.findReconnectPendingUserIdsCallCount).isEqualTo(1);
        assertThat(trackingPort.isReconnectPendingCallCount).isZero();
    }

    private static final class MutableClock extends Clock {

        private Instant currentInstant;

        private MutableClock(Instant currentInstant) {
            this.currentInstant = currentInstant;
        }

        @Override
        public ZoneOffset getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(java.time.ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return currentInstant;
        }

        private void plusSeconds(long seconds) {
            currentInstant = currentInstant.plusSeconds(seconds);
        }
    }

    private static final class InMemoryPresenceReconnectStatePort implements PresenceReconnectStatePort {

        private final Map<Long, Instant> reconnectDeadlineByUserId = new ConcurrentHashMap<>();

        @Override
        public void markReconnectPending(Long userId, Instant reconnectDeadline) {
            if (userId == null || reconnectDeadline == null) {
                return;
            }
            reconnectDeadlineByUserId.put(userId, reconnectDeadline);
        }

        @Override
        public void clearReconnectPending(Long userId) {
            if (userId == null) {
                return;
            }
            reconnectDeadlineByUserId.remove(userId);
        }

        @Override
        public boolean isReconnectPending(Long userId) {
            if (userId == null) {
                return false;
            }
            return reconnectDeadlineByUserId.containsKey(userId);
        }

        @Override
        public Set<Long> findReconnectPendingUserIds(Collection<Long> userIds) {
            if (userIds == null || userIds.isEmpty()) {
                return Set.of();
            }
            return userIds.stream()
                .filter(reconnectDeadlineByUserId::containsKey)
                .collect(java.util.stream.Collectors.toSet());
        }

        @Override
        public List<Long> findExpiredReconnectUserIds(Instant deadlineInclusive) {
            if (deadlineInclusive == null) {
                return List.of();
            }
            return reconnectDeadlineByUserId.entrySet().stream()
                .filter(entry -> !entry.getValue().isAfter(deadlineInclusive))
                .map(Map.Entry::getKey)
                .toList();
        }
    }

    private static final class TrackingPresenceReconnectStatePort implements PresenceReconnectStatePort {

        private final Set<Long> reconnectingUserIds;
        private int isReconnectPendingCallCount;
        private int findReconnectPendingUserIdsCallCount;

        private TrackingPresenceReconnectStatePort(Set<Long> reconnectingUserIds) {
            this.reconnectingUserIds = reconnectingUserIds;
        }

        @Override
        public void markReconnectPending(Long userId, Instant reconnectDeadline) {
        }

        @Override
        public void clearReconnectPending(Long userId) {
        }

        @Override
        public boolean isReconnectPending(Long userId) {
            isReconnectPendingCallCount++;
            return reconnectingUserIds.contains(userId);
        }

        @Override
        public Set<Long> findReconnectPendingUserIds(Collection<Long> userIds) {
            findReconnectPendingUserIdsCallCount++;
            if (userIds == null || userIds.isEmpty()) {
                return Set.of();
            }
            return userIds.stream()
                .filter(reconnectingUserIds::contains)
                .collect(java.util.stream.Collectors.toSet());
        }

        @Override
        public List<Long> findExpiredReconnectUserIds(Instant deadlineInclusive) {
            return List.of();
        }

        private void resetCounters() {
            isReconnectPendingCallCount = 0;
            findReconnectPendingUserIdsCallCount = 0;
        }
    }

    private static final class BlockingPresenceReconnectStatePort implements PresenceReconnectStatePort {

        private final Map<Long, Instant> reconnectDeadlineByUserId = new ConcurrentHashMap<>();
        private final CountDownLatch singleLookupStarted = new CountDownLatch(1);
        private final CountDownLatch batchLookupStarted = new CountDownLatch(1);
        private final CountDownLatch allowLookupToReturn = new CountDownLatch(1);
        private final CountDownLatch clearCalled = new CountDownLatch(1);

        private BlockingPresenceReconnectStatePort(Set<Long> reconnectingUserIds) {
            reconnectingUserIds.forEach(userId -> reconnectDeadlineByUserId.put(userId, BASE_TIME.plusSeconds(60)));
        }

        @Override
        public void markReconnectPending(Long userId, Instant reconnectDeadline) {
            if (userId == null || reconnectDeadline == null) {
                return;
            }
            reconnectDeadlineByUserId.put(userId, reconnectDeadline);
        }

        @Override
        public void clearReconnectPending(Long userId) {
            clearCalled.countDown();
            if (userId == null) {
                return;
            }
            reconnectDeadlineByUserId.remove(userId);
        }

        @Override
        public boolean isReconnectPending(Long userId) {
            singleLookupStarted.countDown();
            awaitLookupRelease();
            if (userId == null) {
                return false;
            }
            return reconnectDeadlineByUserId.containsKey(userId);
        }

        @Override
        public Set<Long> findReconnectPendingUserIds(Collection<Long> userIds) {
            batchLookupStarted.countDown();
            awaitLookupRelease();
            if (userIds == null || userIds.isEmpty()) {
                return Set.of();
            }
            return userIds.stream()
                .filter(reconnectDeadlineByUserId::containsKey)
                .collect(java.util.stream.Collectors.toSet());
        }

        @Override
        public List<Long> findExpiredReconnectUserIds(Instant deadlineInclusive) {
            return List.of();
        }

        private boolean awaitSingleLookupStarted() throws InterruptedException {
            return singleLookupStarted.await(1, TimeUnit.SECONDS);
        }

        private boolean awaitBatchLookupStarted() throws InterruptedException {
            return batchLookupStarted.await(1, TimeUnit.SECONDS);
        }

        private boolean awaitClearCalled(long timeoutMillis) throws InterruptedException {
            return clearCalled.await(timeoutMillis, TimeUnit.MILLISECONDS);
        }

        private void releaseLookup() {
            allowLookupToReturn.countDown();
        }

        private void awaitLookupRelease() {
            try {
                if (!allowLookupToReturn.await(1, TimeUnit.SECONDS)) {
                    throw new IllegalStateException("Timed out waiting for test lookup release");
                }
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(exception);
            }
        }
    }
}
