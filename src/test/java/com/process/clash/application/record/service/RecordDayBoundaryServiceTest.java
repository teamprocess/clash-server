package com.process.clash.application.record.service;

import com.process.clash.application.record.port.out.RecordSessionRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.record.entity.RecordSession;
import com.process.clash.domain.record.entity.RecordTask;
import com.process.clash.domain.record.enums.RecordType;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import com.process.clash.infrastructure.config.RecordProperties;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecordDayBoundaryServiceTest {

    private static final ZoneId RECORD_ZONE_ID = ZoneId.of("UTC");

    @Mock
    private RecordSessionRepositoryPort recordSessionRepositoryPort;

    private RecordDayBoundaryService recordDayBoundaryService;

    @BeforeEach
    void setUp() {
        RecordProperties recordProperties = new RecordProperties(RECORD_ZONE_ID.getId(), 6);
        recordDayBoundaryService = new RecordDayBoundaryService(
            recordSessionRepositoryPort,
            recordProperties,
            RECORD_ZONE_ID
        );
    }

    @Test
    @DisplayName("경계 롤오버 시 saveAll 사이에서 flush를 한 번 호출한다")
    void rolloverActiveSessionsAtBoundary_flushesOnceBetweenSaveAll() {
        User user = createUser(1L);
        RecordTask task = createTask(11L, user);
        RecordSession activeSession = RecordSession.create(
            100L,
            user,
            task,
            RecordType.TASK,
            null,
            Instant.now().minusSeconds(60L * 60L * 30L),
            null
        );

        when(recordSessionRepositoryPort.findAllActiveSessions()).thenReturn(List.of(activeSession));

        recordDayBoundaryService.rolloverActiveSessionsAtBoundary();

        InOrder inOrder = inOrder(recordSessionRepositoryPort);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<RecordSession>> savedSessionsCaptor = ArgumentCaptor.forClass(List.class);

        inOrder.verify(recordSessionRepositoryPort).findAllActiveSessions();
        inOrder.verify(recordSessionRepositoryPort).saveAll(savedSessionsCaptor.capture());
        inOrder.verify(recordSessionRepositoryPort).flush();
        inOrder.verify(recordSessionRepositoryPort).saveAll(savedSessionsCaptor.capture());

        List<List<RecordSession>> allSavedSessions = savedSessionsCaptor.getAllValues();
        List<RecordSession> sessionsToClose = allSavedSessions.get(0);
        List<RecordSession> sessionsToCreate = allSavedSessions.get(1);

        assertThat(sessionsToClose).singleElement().satisfies(session -> {
            assertThat(session.id()).isEqualTo(activeSession.id());
            assertThat(session.endedAt()).isNotNull();
        });
        assertThat(sessionsToCreate).isNotEmpty();
        assertThat(sessionsToCreate).allSatisfy(session -> assertThat(session.id()).isNull());
    }

    @Test
    @DisplayName("롤오버 대상이 없으면 flush를 호출하지 않는다")
    void rolloverActiveSessionsAtBoundary_doesNotFlushWithoutRolloverTarget() {
        when(recordSessionRepositoryPort.findAllActiveSessions()).thenReturn(List.of());

        recordDayBoundaryService.rolloverActiveSessionsAtBoundary();

        verify(recordSessionRepositoryPort, times(2)).saveAll(anyList());
        verify(recordSessionRepositoryPort, never()).flush();
    }

    private User createUser(Long id) {
        return new User(
            id,
            Instant.now(),
            Instant.now(),
            "username",
            "user@example.com",
            "name",
            "encoded-password",
            Role.USER,
            "",
            0,
            0,
            0,
            Major.NONE,
            UserStatus.ACTIVE
        );
    }

    private RecordTask createTask(Long id, User user) {
        return new RecordTask(
            id,
            "task",
            0L,
            Instant.now(),
            Instant.now(),
            user
        );
    }
}
