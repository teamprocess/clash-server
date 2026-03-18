package com.process.clash.application.record.v2.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.v2.data.UpdateTaskCompletionV2Data;
import com.process.clash.application.record.v2.exception.exception.notfound.TaskV2NotFoundException;
import com.process.clash.application.record.v2.port.out.RecordActivityNotifierPort;
import com.process.clash.application.record.v2.port.out.RecordDevelopSessionSegmentV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordTaskV2RepositoryPort;
import com.process.clash.application.user.exp.service.StudyTimeExpGrantService;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.entity.RecordTaskV2;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateTaskCompletionV2ServiceTest {

    @Mock
    private RecordTaskV2RepositoryPort recordTaskV2RepositoryPort;

    @Mock
    private RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;

    @Mock
    private RecordDevelopSessionSegmentV2RepositoryPort recordDevelopSessionSegmentV2RepositoryPort;

    @Mock
    private RecordActivityNotifierPort recordActivityNotifierPort;

    @Mock
    private StudyTimeExpGrantService studyTimeExpGrantService;

    private UpdateTaskCompletionV2Service updateTaskCompletionV2Service;

    @BeforeEach
    void setUp() {
        updateTaskCompletionV2Service = new UpdateTaskCompletionV2Service(
            recordTaskV2RepositoryPort,
            recordSessionV2RepositoryPort,
            recordDevelopSessionSegmentV2RepositoryPort,
            recordActivityNotifierPort,
            studyTimeExpGrantService
        );
    }

    @Test
    @DisplayName("본인 task의 완료 상태를 변경한다")
    void execute_updatesCompletion() {
        Actor actor = new Actor(1L);
        RecordTaskV2 task = new RecordTaskV2(11L, 1L, null, "리팩터링", false, 0L, LocalDate.of(2026, 3, 16), Instant.now(), Instant.now());
        UpdateTaskCompletionV2Data.Command command = new UpdateTaskCompletionV2Data.Command(actor, 11L, true);

        when(recordTaskV2RepositoryPort.findByIdAndUserId(11L, 1L)).thenReturn(Optional.of(task));
        when(recordTaskV2RepositoryPort.save(any(RecordTaskV2.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        when(recordSessionV2RepositoryPort.findActiveSessionByUserIdForUpdate(1L))
            .thenReturn(Optional.empty());

        UpdateTaskCompletionV2Data.Result result = updateTaskCompletionV2Service.execute(command);

        assertThat(result.task().completed()).isTrue();
        verify(recordTaskV2RepositoryPort).save(any(RecordTaskV2.class));
    }

    @Test
    @DisplayName("완료 처리 시 같은 task의 진행 중 세션이 있으면 종료한다")
    void execute_stopsActiveSessionWhenCompletingActiveTask() {
        Actor actor = new Actor(1L);
        Instant startedAt = Instant.now().minusSeconds(300);
        RecordTaskV2 task = new RecordTaskV2(11L, 1L, null, "리팩터링", false, 0L, LocalDate.of(2026, 3, 16), Instant.now(), Instant.now());
        RecordSessionV2 activeSession = new RecordSessionV2(
            100L,
            1L,
            com.process.clash.domain.record.v2.enums.RecordSessionTypeV2.TASK,
            null,
            null,
            11L,
            "리팩터링",
            null,
            startedAt,
            null
        );
        UpdateTaskCompletionV2Data.Command command = new UpdateTaskCompletionV2Data.Command(actor, 11L, true);

        when(recordTaskV2RepositoryPort.findByIdAndUserId(11L, 1L)).thenReturn(Optional.of(task));
        when(recordTaskV2RepositoryPort.save(any(RecordTaskV2.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        when(recordSessionV2RepositoryPort.findActiveSessionByUserIdForUpdate(1L))
            .thenReturn(Optional.of(activeSession));
        when(recordSessionV2RepositoryPort.save(any(RecordSessionV2.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        UpdateTaskCompletionV2Data.Result result = updateTaskCompletionV2Service.execute(command);

        assertThat(result.task().completed()).isTrue();
        verify(recordSessionV2RepositoryPort).save(
            argThat(session -> session.id().equals(100L) && session.endedAt() != null)
        );
        verify(recordActivityNotifierPort).notifyActivityStopped(actor);
        verify(studyTimeExpGrantService).grant(eq(1L), eq(startedAt), any(Instant.class));
    }

    @Test
    @DisplayName("미완료 처리이거나 다른 task 세션이면 종료하지 않는다")
    void execute_doesNotStopSessionWhenNotCompletingActiveTask() {
        Actor actor = new Actor(1L);
        RecordTaskV2 task = new RecordTaskV2(11L, 1L, null, "리팩터링", true, 0L, LocalDate.of(2026, 3, 16), Instant.now(), Instant.now());
        UpdateTaskCompletionV2Data.Command command = new UpdateTaskCompletionV2Data.Command(actor, 11L, false);

        when(recordTaskV2RepositoryPort.findByIdAndUserId(11L, 1L)).thenReturn(Optional.of(task));
        when(recordTaskV2RepositoryPort.save(any(RecordTaskV2.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        UpdateTaskCompletionV2Data.Result result = updateTaskCompletionV2Service.execute(command);

        assertThat(result.task().completed()).isFalse();
        verify(recordSessionV2RepositoryPort, never()).findActiveSessionByUserIdForUpdate(any());
        verify(recordSessionV2RepositoryPort, never()).save(any(RecordSessionV2.class));
        verify(recordActivityNotifierPort, never()).notifyActivityStopped(any());
        verify(studyTimeExpGrantService, never()).grant(any(), any(), any());
    }

    @Test
    @DisplayName("타인 task거나 없는 task면 완료 상태를 변경할 수 없다")
    void execute_throwsWhenTaskNotFound() {
        Actor actor = new Actor(1L);
        UpdateTaskCompletionV2Data.Command command = new UpdateTaskCompletionV2Data.Command(actor, 11L, true);

        when(recordTaskV2RepositoryPort.findByIdAndUserId(11L, 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateTaskCompletionV2Service.execute(command))
            .isInstanceOf(TaskV2NotFoundException.class);
    }
}
