package com.process.clash.application.realtime.service;

import com.process.clash.application.realtime.data.UserActivityStatus;
import com.process.clash.application.realtime.port.in.ReportUserPresenceUseCase;
import com.process.clash.application.realtime.port.out.NotifyPresenceStatusChangedPort;
import com.process.clash.application.realtime.port.out.UserPresencePort;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPresenceService implements ReportUserPresenceUseCase, UserPresencePort {

    private final NotifyPresenceStatusChangedPort notifyPresenceStatusChangedPort;
    private final Object monitor = new Object();
    private final Map<String, SessionPresence> sessionByConnectionId = new HashMap<>();
    private final Map<Long, PresenceCounter> counterByUserId = new HashMap<>();

    @Override
    public void connected(String connectionId, Long userId) {
        if (isInvalid(connectionId, userId)) {
            return;
        }

        List<StatusChange> statusChanges;
        synchronized (monitor) {
            SessionPresence previous = sessionByConnectionId.get(connectionId);
            Set<Long> impactedUserIds = new LinkedHashSet<>();
            if (previous != null) {
                impactedUserIds.add(previous.userId());
            }
            impactedUserIds.add(userId);

            Map<Long, UserActivityStatus> beforeStatuses = snapshotStatuses(impactedUserIds);

            SessionPresence replaced = sessionByConnectionId.put(
                connectionId,
                SessionPresence.online(userId)
            );

            if (replaced != null) {
                decrease(replaced.userId(), replaced.away());
            }
            increase(userId, false);

            statusChanges = collectStatusChanges(impactedUserIds, beforeStatuses);
        }
        dispatchStatusChanges(statusChanges);
    }

    @Override
    public void disconnected(String connectionId) {
        if (isBlank(connectionId)) {
            return;
        }

        List<StatusChange> statusChanges;
        synchronized (monitor) {
            SessionPresence current = sessionByConnectionId.get(connectionId);
            if (current == null) {
                return;
            }

            Set<Long> impactedUserIds = Set.of(current.userId());
            Map<Long, UserActivityStatus> beforeStatuses = snapshotStatuses(impactedUserIds);

            sessionByConnectionId.remove(connectionId);
            decrease(current.userId(), current.away());
            statusChanges = collectStatusChanges(impactedUserIds, beforeStatuses);
        }
        dispatchStatusChanges(statusChanges);
    }

    @Override
    public void markedAway(String connectionId) {
        markAwayState(connectionId, true);
    }

    @Override
    public void markedOnline(String connectionId) {
        markAwayState(connectionId, false);
    }

    @Override
    public UserActivityStatus getStatus(Long userId) {
        if (userId == null) {
            return UserActivityStatus.OFFLINE;
        }

        synchronized (monitor) {
            return resolveStatus(counterByUserId.get(userId));
        }
    }

    @Override
    public Map<Long, UserActivityStatus> getStatuses(Collection<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Map.of();
        }

        synchronized (monitor) {
            return userIds.stream()
                .filter(java.util.Objects::nonNull)
                .collect(java.util.stream.Collectors.toUnmodifiableMap(
                    userId -> userId,
                    userId -> resolveStatus(counterByUserId.get(userId))
                ));
        }
    }

    private void markAwayState(String connectionId, boolean away) {
        if (isBlank(connectionId)) {
            return;
        }

        List<StatusChange> statusChanges;
        synchronized (monitor) {
            SessionPresence current = sessionByConnectionId.get(connectionId);
            if (current == null || current.away() == away) {
                return;
            }

            Set<Long> impactedUserIds = Set.of(current.userId());
            Map<Long, UserActivityStatus> beforeStatuses = snapshotStatuses(impactedUserIds);

            sessionByConnectionId.put(connectionId, current.changeAway(away));
            if (away) {
                increaseAway(current.userId());
            } else {
                decreaseAway(current.userId());
            }

            statusChanges = collectStatusChanges(impactedUserIds, beforeStatuses);
        }
        dispatchStatusChanges(statusChanges);
    }

    private void increase(Long userId, boolean away) {
        PresenceCounter counter = counterByUserId.get(userId);
        if (counter == null) {
            counter = new PresenceCounter();
            counterByUserId.put(userId, counter);
        }
        counter.connectedSessions++;
        if (away) {
            counter.awaySessions++;
        }
    }

    private void decrease(Long userId, boolean away) {
        PresenceCounter counter = counterByUserId.get(userId);
        if (counter == null) {
            return;
        }

        counter.connectedSessions = Math.max(0, counter.connectedSessions - 1);
        if (away) {
            counter.awaySessions = Math.max(0, counter.awaySessions - 1);
        }

        if (counter.connectedSessions == 0) {
            counterByUserId.remove(userId);
            return;
        }

        if (counter.awaySessions > counter.connectedSessions) {
            counter.awaySessions = counter.connectedSessions;
        }
    }

    private void increaseAway(Long userId) {
        PresenceCounter counter = counterByUserId.get(userId);
        if (counter == null || counter.connectedSessions == 0) {
            return;
        }
        counter.awaySessions = Math.min(counter.connectedSessions, counter.awaySessions + 1);
    }

    private void decreaseAway(Long userId) {
        PresenceCounter counter = counterByUserId.get(userId);
        if (counter == null) {
            return;
        }
        counter.awaySessions = Math.max(0, counter.awaySessions - 1);
    }

    private UserActivityStatus resolveStatus(PresenceCounter counter) {
        if (counter == null || counter.connectedSessions == 0) {
            return UserActivityStatus.OFFLINE;
        }
        if (counter.awaySessions >= counter.connectedSessions) {
            return UserActivityStatus.AWAY;
        }
        return UserActivityStatus.ONLINE;
    }

    private boolean isInvalid(String connectionId, Long userId) {
        return isBlank(connectionId) || userId == null;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private Map<Long, UserActivityStatus> snapshotStatuses(Collection<Long> userIds) {
        Map<Long, UserActivityStatus> snapshot = new HashMap<>();
        if (userIds == null) {
            return snapshot;
        }

        for (Long userId : userIds) {
            if (userId == null) {
                continue;
            }
            snapshot.put(userId, resolveStatus(counterByUserId.get(userId)));
        }
        return snapshot;
    }

    private List<StatusChange> collectStatusChanges(
        Collection<Long> userIds,
        Map<Long, UserActivityStatus> beforeStatuses
    ) {
        List<StatusChange> changes = new ArrayList<>();
        if (userIds == null) {
            return changes;
        }

        for (Long userId : userIds) {
            if (userId == null) {
                continue;
            }
            UserActivityStatus previous = beforeStatuses.getOrDefault(userId, UserActivityStatus.OFFLINE);
            UserActivityStatus current = resolveStatus(counterByUserId.get(userId));
            if (previous != current) {
                changes.add(new StatusChange(userId, previous, current));
            }
        }
        return changes;
    }

    private void dispatchStatusChanges(List<StatusChange> statusChanges) {
        if (statusChanges == null || statusChanges.isEmpty()) {
            return;
        }

        for (StatusChange change : statusChanges) {
            notifyPresenceStatusChangedPort.notifyStatusChanged(
                change.userId(),
                change.previousStatus(),
                change.currentStatus()
            );
        }
    }

    private record SessionPresence(Long userId, boolean away) {
        private static SessionPresence online(Long userId) {
            return new SessionPresence(userId, false);
        }

        private SessionPresence changeAway(boolean away) {
            return new SessionPresence(this.userId, away);
        }
    }

    private static final class PresenceCounter {
        private int connectedSessions;
        private int awaySessions;
    }

    private record StatusChange(
        Long userId,
        UserActivityStatus previousStatus,
        UserActivityStatus currentStatus
    ) {}
}
