package com.process.clash.application.record.service;

import com.process.clash.application.record.port.out.RecordActivityNotifierPort;
import com.process.clash.application.record.port.out.RecordSessionRepositoryPort;
import com.process.clash.application.user.exp.service.StudyTimeExpGrantService;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.record.entity.RecordSession;
import com.process.clash.domain.record.entity.RecordTask;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import com.process.clash.infrastructure.config.record.RecordProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecordDayBoundaryServiceTest {

    @Mock
    private RecordSessionRepositoryPort recordSessionRepositoryPort;

    @Mock
    private RecordActivityNotifierPort recordActivityNotifierPort;

    @Mock
    private StudyTimeExpGrantService studyTimeExpGrantService;

    private RecordDayBoundaryService recordDayBoundaryService;

    private static final ZoneId ZONE = ZoneId.of("Asia/Seoul");
    private static final int BOUNDARY_HOUR = 6;

    @BeforeEach
    void setUp() {
        recordDayBoundaryService = new RecordDayBoundaryService(
            recordSessionRepositoryPort,
            recordActivityNotifierPort,
            studyTimeExpGrantService,
            new RecordProperties("Asia/Seoul", BOUNDARY_HOUR),
            ZONE
        );
    }

    @Test
    @DisplayName("활성 세션이 없으면 EXP 지급을 호출하지 않는다")
    void rollover_doesNotGrantExpWhenNoActiveSessions() {
        when(recordSessionRepositoryPort.findAllActiveSessions()).thenReturn(List.of());

        recordDayBoundaryService.rolloverActiveSessionsAtBoundary();

        verify(studyTimeExpGrantService, never()).grant(any(), any(), any());
    }

    @Test
    @DisplayName("경계 시각이 지나지 않은 세션은 처리하지 않는다")
    void rollover_skipsSessionsWhoseBoundaryHasNotPassed() {
        // 지금 막 시작된 세션 (경계 시각이 아직 안 지남)
        Instant justNow = Instant.now().minusSeconds(10);
        RecordSession recentSession = RecordSession.create(1L, createUser(1L), null, justNow, null);

        when(recordSessionRepositoryPort.findAllActiveSessions()).thenReturn(List.of(recentSession));

        recordDayBoundaryService.rolloverActiveSessionsAtBoundary();

        verify(studyTimeExpGrantService, never()).grant(any(), any(), any());
    }

    @Test
    @DisplayName("경계 시각이 지난 세션은 경계 시각에 닫고 새 세션을 생성한다")
    void rollover_closesSessionAtBoundaryAndCreatesNewOne() {
        // 어제 오전 10시에 시작 (어제 학습일에 속함)
        Instant startedAt = Instant.now()
            .atZone(ZONE)
            .minusDays(1)
            .withHour(10).withMinute(0).withSecond(0).withNano(0)
            .toInstant();
        RecordSession activeSession = RecordSession.create(1L, createUser(1L), null, startedAt, null);

        when(recordSessionRepositoryPort.findAllActiveSessions()).thenReturn(List.of(activeSession));
        doNothing().when(recordSessionRepositoryPort).saveAll(anyList());

        recordDayBoundaryService.rolloverActiveSessionsAtBoundary();

        // sessionsToClose: 원본 세션을 경계 시각에 종료
        // sessionsToCreate: 경계 시각부터 현재까지의 새 세션
        verify(recordSessionRepositoryPort, times(2)).saveAll(anyList());
    }

    @Test
    @DisplayName("경계 시각이 지난 세션에 대해 학습시간 EXP 지급을 호출한다")
    void rollover_callsStudyTimeExpGrantForClosedSession() {
        Instant startedAt = Instant.now()
            .atZone(ZONE)
            .minusDays(1)
            .withHour(10).withMinute(0).withSecond(0).withNano(0)
            .toInstant();
        User user = createUser(1L);
        RecordSession activeSession = RecordSession.create(1L, user, null, startedAt, null);

        when(recordSessionRepositoryPort.findAllActiveSessions()).thenReturn(List.of(activeSession));
        doNothing().when(recordSessionRepositoryPort).saveAll(anyList());

        recordDayBoundaryService.rolloverActiveSessionsAtBoundary();

        // 원본 세션이 경계 시각에 닫히므로 EXP grant 1회 이상 호출
        verify(studyTimeExpGrantService, atLeastOnce()).grant(eq(1L), any(Instant.class), any(Instant.class));
    }

    @Test
    @DisplayName("EXP 지급 실패 시 다른 세션 처리는 계속된다")
    void rollover_continuesWhenExpGrantFails() {
        Instant startedAt = Instant.now()
            .atZone(ZONE)
            .minusDays(1)
            .withHour(10).withMinute(0).withSecond(0).withNano(0)
            .toInstant();
        User user1 = createUser(1L);
        User user2 = createUser(2L);
        RecordSession session1 = RecordSession.create(1L, user1, null, startedAt, null);
        RecordSession session2 = RecordSession.create(2L, user2, null, startedAt, null);

        when(recordSessionRepositoryPort.findAllActiveSessions()).thenReturn(List.of(session1, session2));
        doNothing().when(recordSessionRepositoryPort).saveAll(anyList());
        doThrow(new RuntimeException("EXP grant error"))
            .when(studyTimeExpGrantService).grant(eq(1L), any(), any());

        // 예외가 외부로 전파되지 않아야 함
        recordDayBoundaryService.rolloverActiveSessionsAtBoundary();

        // user2도 EXP grant 시도됨
        verify(studyTimeExpGrantService, atLeastOnce()).grant(eq(2L), any(Instant.class), any(Instant.class));
    }

    // --- helper ---

    private User createUser(Long id) {
        return new User(id, Instant.now(), Instant.now(), "user" + id, "user@example.com",
            "name", "pw", Role.USER, "", 0, 0, Major.NONE, UserStatus.ACTIVE, null);
    }
}
