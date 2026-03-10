package com.process.clash.application.record.v2.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.v2.data.DeleteTaskV2Data;
import com.process.clash.application.record.v2.exception.exception.conflict.TaskV2HasActiveSessionException;
import com.process.clash.application.record.v2.exception.exception.notfound.TaskV2NotFoundException;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
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
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
class DeleteTaskV2ServiceTest {

    @Mock
    private RecordTaskV2RepositoryPort recordTaskV2RepositoryPort;

    @Mock
    private RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;

    private DeleteTaskV2Service deleteTaskV2Service;

    @BeforeEach
    void setUp() {
        deleteTaskV2Service = new DeleteTaskV2Service(
            recordTaskV2RepositoryPort,
            recordSessionV2RepositoryPort
        );
    }

    @Test
    @DisplayName("부모 없는 세부 작업도 진행중인 세션이 없으면 삭제할 수 있다")
    void execute_deletesUngroupedTaskWhenNoActiveSession() {
        Actor actor = new Actor(1L);
        RecordTaskV2 task = new RecordTaskV2(11L, 1L, null, "리팩터링", false, 0L, Instant.now(), Instant.now());
        DeleteTaskV2Data.Command command = new DeleteTaskV2Data.Command(actor, 11L);

        when(recordTaskV2RepositoryPort.findByIdAndUserId(11L, 1L)).thenReturn(Optional.of(task));
        when(recordSessionV2RepositoryPort.existsActiveSessionByTaskId(11L)).thenReturn(false);

        deleteTaskV2Service.execute(command);

        verify(recordTaskV2RepositoryPort).deleteById(11L);
    }

    @Test
    @DisplayName("진행중인 세션이 있으면 삭제할 수 없다")
    void execute_throwsWhenActiveSessionExists() {
        Actor actor = new Actor(1L);
        RecordTaskV2 task = new RecordTaskV2(11L, 1L, null, "리팩터링", false, 0L, Instant.now(), Instant.now());
        DeleteTaskV2Data.Command command = new DeleteTaskV2Data.Command(actor, 11L);

        when(recordTaskV2RepositoryPort.findByIdAndUserId(11L, 1L)).thenReturn(Optional.of(task));
        when(recordSessionV2RepositoryPort.existsActiveSessionByTaskId(11L)).thenReturn(true);

        assertThatThrownBy(() -> deleteTaskV2Service.execute(command))
            .isInstanceOf(TaskV2HasActiveSessionException.class);
        verify(recordTaskV2RepositoryPort, never()).deleteById(11L);
    }

    @Test
    @DisplayName("task가 없으면 예외가 발생한다")
    void execute_throwsWhenTaskNotFound() {
        Actor actor = new Actor(1L);
        DeleteTaskV2Data.Command command = new DeleteTaskV2Data.Command(actor, 11L);

        when(recordTaskV2RepositoryPort.findByIdAndUserId(11L, 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteTaskV2Service.execute(command))
            .isInstanceOf(TaskV2NotFoundException.class);
    }

    @Test
    @DisplayName("삭제 시 FK 무결성 예외가 발생하면 도메인 예외로 변환한다")
    void execute_throwsDomainConflictWhenDeleteFailsByIntegrity() {
        Actor actor = new Actor(1L);
        RecordTaskV2 task = new RecordTaskV2(11L, 1L, null, "리팩터링", false, 0L, Instant.now(), Instant.now());
        DeleteTaskV2Data.Command command = new DeleteTaskV2Data.Command(actor, 11L);

        when(recordTaskV2RepositoryPort.findByIdAndUserId(11L, 1L)).thenReturn(Optional.of(task));
        when(recordSessionV2RepositoryPort.existsActiveSessionByTaskId(11L)).thenReturn(false);
        org.mockito.Mockito.doThrow(new DataIntegrityViolationException("fk constraint"))
            .when(recordTaskV2RepositoryPort).deleteById(11L);

        assertThatThrownBy(() -> deleteTaskV2Service.execute(command))
            .isInstanceOf(TaskV2HasActiveSessionException.class);
    }
}
