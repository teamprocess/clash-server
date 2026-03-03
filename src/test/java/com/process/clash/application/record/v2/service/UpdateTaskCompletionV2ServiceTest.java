package com.process.clash.application.record.v2.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.v2.data.UpdateTaskCompletionV2Data;
import com.process.clash.application.record.v2.exception.exception.notfound.TaskV2NotFoundException;
import com.process.clash.application.record.v2.port.out.RecordTaskV2RepositoryPort;
import com.process.clash.domain.record.v2.entity.RecordTaskV2;
import java.time.Instant;
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

    private UpdateTaskCompletionV2Service updateTaskCompletionV2Service;

    @BeforeEach
    void setUp() {
        updateTaskCompletionV2Service = new UpdateTaskCompletionV2Service(recordTaskV2RepositoryPort);
    }

    @Test
    @DisplayName("본인 task의 완료 상태를 변경한다")
    void execute_updatesCompletion() {
        Actor actor = new Actor(1L);
        RecordTaskV2 task = new RecordTaskV2(11L, 1L, null, "리팩터링", false, 0L, Instant.now(), Instant.now());
        UpdateTaskCompletionV2Data.Command command = new UpdateTaskCompletionV2Data.Command(actor, 11L, true);

        when(recordTaskV2RepositoryPort.findByIdAndUserId(11L, 1L)).thenReturn(Optional.of(task));
        when(recordTaskV2RepositoryPort.save(any(RecordTaskV2.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        UpdateTaskCompletionV2Data.Result result = updateTaskCompletionV2Service.execute(command);

        assertThat(result.task().completed()).isTrue();
        verify(recordTaskV2RepositoryPort).save(any(RecordTaskV2.class));
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
