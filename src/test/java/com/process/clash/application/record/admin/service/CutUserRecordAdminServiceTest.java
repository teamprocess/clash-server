package com.process.clash.application.record.admin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.admin.data.CutUserRecordAdminData;
import com.process.clash.application.record.port.out.RecordActivityNotifierPort;
import com.process.clash.application.record.v2.exception.exception.notfound.ActiveSessionV2NotFoundException;
import com.process.clash.application.record.v2.port.out.RecordDevelopSessionSegmentV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.application.user.exp.service.StudyTimeExpGrantService;
import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.record.v2.entity.RecordDevelopSessionSegmentV2;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CutUserRecordAdminServiceTest {

    @Mock
    private RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;

    @Mock
    private RecordDevelopSessionSegmentV2RepositoryPort recordDevelopSessionSegmentV2RepositoryPort;

    @Mock
    private RecordActivityNotifierPort recordActivityNotifierPort;

    @Mock
    private StudyTimeExpGrantService studyTimeExpGrantService;

    private CutUserRecordAdminService cutUserRecordAdminService;

    @BeforeEach
    void setUp() {
        cutUserRecordAdminService = new CutUserRecordAdminService(
            recordSessionV2RepositoryPort,
            recordDevelopSessionSegmentV2RepositoryPort,
            recordActivityNotifierPort
        );
    }

    @Test
    @DisplayName("활성 세션이 없으면 예외가 발생한다")
    void execute_throwsWhenNoActiveSession() {
        CutUserRecordAdminData.Command command = new CutUserRecordAdminData.Command(1L);

        when(recordSessionV2RepositoryPort.findActiveSessionByUserIdForUpdate(1L))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> cutUserRecordAdminService.execute(command))
            .isInstanceOf(ActiveSessionV2NotFoundException.class);

        verify(recordActivityNotifierPort, never()).notifyActivityStopped(any());
    }

    @Test
    @DisplayName("TASK 세션 강제 중단 시 ended_at을 저장하고 알림을 전송한다")
    void execute_stopsTaskSessionAndNotifies() {
        Long userId = 1L;
        CutUserRecordAdminData.Command command = new CutUserRecordAdminData.Command(userId);

        RecordSessionV2 activeSession = new RecordSessionV2(
            100L, userId, RecordSessionTypeV2.TASK, 10L, "백엔드 프로젝트", 20L, "ERD 설계",
            null, Instant.now().minusSeconds(600), null
        );

        when(recordSessionV2RepositoryPort.findActiveSessionByUserIdForUpdate(userId))
            .thenReturn(Optional.of(activeSession));
        when(recordSessionV2RepositoryPort.save(any(RecordSessionV2.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        CutUserRecordAdminData.Result result = cutUserRecordAdminService.execute(command);

        verify(recordSessionV2RepositoryPort).save(argThat(session -> session.endedAt() != null));
        verify(recordActivityNotifierPort).notifyActivityStopped(argThat(actor -> actor.id().equals(userId)));
        assertThat(result.endedAt()).isNotNull();
        assertThat(result.session()).isNotNull();
    }

    @Test
    @DisplayName("TASK 세션 강제 중단 시 개발 세그먼트를 건드리지 않는다")
    void execute_doesNotTouchSegmentWhenTaskSession() {
        Long userId = 1L;
        CutUserRecordAdminData.Command command = new CutUserRecordAdminData.Command(userId);

        RecordSessionV2 activeSession = new RecordSessionV2(
            100L, userId, RecordSessionTypeV2.TASK, 10L, "백엔드 프로젝트", 20L, "ERD 설계",
            null, Instant.now().minusSeconds(600), null
        );

        when(recordSessionV2RepositoryPort.findActiveSessionByUserIdForUpdate(userId))
            .thenReturn(Optional.of(activeSession));
        when(recordSessionV2RepositoryPort.save(any(RecordSessionV2.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        cutUserRecordAdminService.execute(command);

        verify(recordDevelopSessionSegmentV2RepositoryPort, never()).findOpenSegmentBySessionIdForUpdate(any());
        verify(recordDevelopSessionSegmentV2RepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("DEVELOP 세션 강제 중단 시 열린 세그먼트를 함께 닫는다")
    void execute_closesOpenSegmentForDevelopSession() {
        Long userId = 1L;
        CutUserRecordAdminData.Command command = new CutUserRecordAdminData.Command(userId);

        RecordSessionV2 activeSession = new RecordSessionV2(
            100L, userId, RecordSessionTypeV2.DEVELOP, null, null, null, null,
            MonitoredApp.VSCODE, Instant.now().minusSeconds(600), null
        );
        RecordDevelopSessionSegmentV2 openSegment = new RecordDevelopSessionSegmentV2(
            200L, 100L, MonitoredApp.VSCODE, Instant.now().minusSeconds(600), null
        );

        when(recordSessionV2RepositoryPort.findActiveSessionByUserIdForUpdate(userId))
            .thenReturn(Optional.of(activeSession));
        when(recordDevelopSessionSegmentV2RepositoryPort.findOpenSegmentBySessionIdForUpdate(100L))
            .thenReturn(Optional.of(openSegment));
        when(recordDevelopSessionSegmentV2RepositoryPort.save(any(RecordDevelopSessionSegmentV2.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        when(recordSessionV2RepositoryPort.save(any(RecordSessionV2.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        cutUserRecordAdminService.execute(command);

        verify(recordDevelopSessionSegmentV2RepositoryPort).save(
            argThat(segment -> segment.endedAt() != null)
        );
        verify(recordSessionV2RepositoryPort).save(argThat(session -> session.endedAt() != null));
        verify(recordActivityNotifierPort).notifyActivityStopped(argThat(actor -> actor.id().equals(userId)));
    }

    @Test
    @DisplayName("DEVELOP 세션에 열린 세그먼트가 없어도 세션은 정상 중단된다")
    void execute_stopsDevelopSessionEvenWithNoOpenSegment() {
        Long userId = 1L;
        CutUserRecordAdminData.Command command = new CutUserRecordAdminData.Command(userId);

        RecordSessionV2 activeSession = new RecordSessionV2(
            100L, userId, RecordSessionTypeV2.DEVELOP, null, null, null, null,
            MonitoredApp.VSCODE, Instant.now().minusSeconds(600), null
        );

        when(recordSessionV2RepositoryPort.findActiveSessionByUserIdForUpdate(userId))
            .thenReturn(Optional.of(activeSession));
        when(recordDevelopSessionSegmentV2RepositoryPort.findOpenSegmentBySessionIdForUpdate(100L))
            .thenReturn(Optional.empty());
        when(recordSessionV2RepositoryPort.save(any(RecordSessionV2.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        CutUserRecordAdminData.Result result = cutUserRecordAdminService.execute(command);

        verify(recordDevelopSessionSegmentV2RepositoryPort, never()).save(any());
        verify(recordSessionV2RepositoryPort).save(argThat(session -> session.endedAt() != null));
        assertThat(result.endedAt()).isNotNull();
    }

    @Test
    @DisplayName("강제 중단 시 EXP를 지급하지 않는다")
    void execute_doesNotGrantExp() {
        Long userId = 1L;
        CutUserRecordAdminData.Command command = new CutUserRecordAdminData.Command(userId);

        RecordSessionV2 activeSession = new RecordSessionV2(
            100L, userId, RecordSessionTypeV2.TASK, 10L, "백엔드 프로젝트", 20L, "ERD 설계",
            null, Instant.now().minusSeconds(600), null
        );

        when(recordSessionV2RepositoryPort.findActiveSessionByUserIdForUpdate(userId))
            .thenReturn(Optional.of(activeSession));
        when(recordSessionV2RepositoryPort.save(any(RecordSessionV2.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        cutUserRecordAdminService.execute(command);

        verify(studyTimeExpGrantService, never()).grant(any(), any(), any());
    }

    @Test
    @DisplayName("반환된 결과에 세션 정보가 포함된다")
    void execute_returnsResultWithSessionInfo() {
        Long userId = 1L;
        CutUserRecordAdminData.Command command = new CutUserRecordAdminData.Command(userId);

        RecordSessionV2 activeSession = new RecordSessionV2(
            100L, userId, RecordSessionTypeV2.TASK, 10L, "백엔드 프로젝트", 20L, "ERD 설계",
            null, Instant.now().minusSeconds(600), null
        );

        when(recordSessionV2RepositoryPort.findActiveSessionByUserIdForUpdate(userId))
            .thenReturn(Optional.of(activeSession));
        when(recordSessionV2RepositoryPort.save(any(RecordSessionV2.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        CutUserRecordAdminData.Result result = cutUserRecordAdminService.execute(command);

        assertThat(result.endedAt()).isNotNull();
        assertThat(result.session()).isNotNull();
        assertThat(result.session().id()).isEqualTo(100L);
    }
}