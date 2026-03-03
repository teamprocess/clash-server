package com.process.clash.application.record.v2.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.v2.data.DeleteSubjectTaskV2Data;
import com.process.clash.application.record.v2.exception.exception.conflict.TaskV2HasActiveSessionException;
import com.process.clash.application.record.v2.exception.exception.forbidden.SubjectV2AccessDeniedException;
import com.process.clash.application.record.v2.exception.exception.notfound.SubjectV2NotFoundException;
import com.process.clash.application.record.v2.exception.exception.notfound.TaskV2NotFoundException;
import com.process.clash.application.record.v2.policy.SubjectV2Policy;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordSubjectV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordTaskV2RepositoryPort;
import com.process.clash.domain.record.v2.entity.RecordSubjectV2;
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
class DeleteSubjectTaskV2ServiceTest {

    @Mock
    private RecordSubjectV2RepositoryPort recordSubjectV2RepositoryPort;

    @Mock
    private RecordTaskV2RepositoryPort recordTaskV2RepositoryPort;

    @Mock
    private RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;

    private DeleteSubjectTaskV2Service deleteSubjectTaskV2Service;

    @BeforeEach
    void setUp() {
        deleteSubjectTaskV2Service = new DeleteSubjectTaskV2Service(
            recordSubjectV2RepositoryPort,
            recordTaskV2RepositoryPort,
            recordSessionV2RepositoryPort,
            new SubjectV2Policy()
        );
    }

    @Test
    @DisplayName("진행중인 세션이 없으면 세부 작업을 삭제한다")
    void execute_deletesTaskWhenNoActiveSession() {
        Actor actor = new Actor(1L);
        RecordSubjectV2 subject = new RecordSubjectV2(10L, 1L, "백엔드", 0L, Instant.now(), Instant.now());
        RecordTaskV2 task = new RecordTaskV2(11L, 1L, 10L, "ERD 설계", false, 0L, Instant.now(), Instant.now());
        DeleteSubjectTaskV2Data.Command command = new DeleteSubjectTaskV2Data.Command(actor, 10L, 11L);

        when(recordSubjectV2RepositoryPort.findById(10L)).thenReturn(Optional.of(subject));
        when(recordTaskV2RepositoryPort.findByIdAndSubjectId(11L, 10L)).thenReturn(Optional.of(task));
        when(recordSessionV2RepositoryPort.existsActiveSessionByTaskId(11L)).thenReturn(false);

        deleteSubjectTaskV2Service.execute(command);

        verify(recordTaskV2RepositoryPort).deleteById(11L);
    }

    @Test
    @DisplayName("진행중인 세션이 있으면 세부 작업을 삭제할 수 없다")
    void execute_throwsWhenActiveSessionExists() {
        Actor actor = new Actor(1L);
        RecordSubjectV2 subject = new RecordSubjectV2(10L, 1L, "백엔드", 0L, Instant.now(), Instant.now());
        RecordTaskV2 task = new RecordTaskV2(11L, 1L, 10L, "ERD 설계", false, 0L, Instant.now(), Instant.now());
        DeleteSubjectTaskV2Data.Command command = new DeleteSubjectTaskV2Data.Command(actor, 10L, 11L);

        when(recordSubjectV2RepositoryPort.findById(10L)).thenReturn(Optional.of(subject));
        when(recordTaskV2RepositoryPort.findByIdAndSubjectId(11L, 10L)).thenReturn(Optional.of(task));
        when(recordSessionV2RepositoryPort.existsActiveSessionByTaskId(11L)).thenReturn(true);

        assertThatThrownBy(() -> deleteSubjectTaskV2Service.execute(command))
            .isInstanceOf(TaskV2HasActiveSessionException.class);
        verify(recordTaskV2RepositoryPort, never()).deleteById(11L);
    }

    @Test
    @DisplayName("과목 그룹이 없으면 예외가 발생한다")
    void execute_throwsWhenSubjectNotFound() {
        Actor actor = new Actor(1L);
        DeleteSubjectTaskV2Data.Command command = new DeleteSubjectTaskV2Data.Command(actor, 10L, 11L);
        when(recordSubjectV2RepositoryPort.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteSubjectTaskV2Service.execute(command))
            .isInstanceOf(SubjectV2NotFoundException.class);
    }

    @Test
    @DisplayName("세부 작업이 없으면 예외가 발생한다")
    void execute_throwsWhenTaskNotFound() {
        Actor actor = new Actor(1L);
        RecordSubjectV2 subject = new RecordSubjectV2(10L, 1L, "백엔드", 0L, Instant.now(), Instant.now());
        DeleteSubjectTaskV2Data.Command command = new DeleteSubjectTaskV2Data.Command(actor, 10L, 11L);

        when(recordSubjectV2RepositoryPort.findById(10L)).thenReturn(Optional.of(subject));
        when(recordTaskV2RepositoryPort.findByIdAndSubjectId(11L, 10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteSubjectTaskV2Service.execute(command))
            .isInstanceOf(TaskV2NotFoundException.class);
    }

    @Test
    @DisplayName("소유자가 아니면 삭제할 수 없다")
    void execute_throwsWhenNotOwner() {
        Actor actor = new Actor(1L);
        RecordSubjectV2 subject = new RecordSubjectV2(10L, 2L, "백엔드", 0L, Instant.now(), Instant.now());
        DeleteSubjectTaskV2Data.Command command = new DeleteSubjectTaskV2Data.Command(actor, 10L, 11L);
        when(recordSubjectV2RepositoryPort.findById(10L)).thenReturn(Optional.of(subject));

        assertThatThrownBy(() -> deleteSubjectTaskV2Service.execute(command))
            .isInstanceOf(SubjectV2AccessDeniedException.class);
        verify(recordTaskV2RepositoryPort, never()).deleteById(11L);
    }

    @Test
    @DisplayName("삭제 시 FK 무결성 예외가 발생하면 도메인 예외로 변환한다")
    void execute_throwsDomainConflictWhenDeleteFailsByIntegrity() {
        Actor actor = new Actor(1L);
        RecordSubjectV2 subject = new RecordSubjectV2(10L, 1L, "백엔드", 0L, Instant.now(), Instant.now());
        RecordTaskV2 task = new RecordTaskV2(11L, 1L, 10L, "ERD 설계", false, 0L, Instant.now(), Instant.now());
        DeleteSubjectTaskV2Data.Command command = new DeleteSubjectTaskV2Data.Command(actor, 10L, 11L);

        when(recordSubjectV2RepositoryPort.findById(10L)).thenReturn(Optional.of(subject));
        when(recordTaskV2RepositoryPort.findByIdAndSubjectId(11L, 10L)).thenReturn(Optional.of(task));
        when(recordSessionV2RepositoryPort.existsActiveSessionByTaskId(11L)).thenReturn(false);
        org.mockito.Mockito.doThrow(new DataIntegrityViolationException("fk constraint"))
            .when(recordTaskV2RepositoryPort).deleteById(11L);

        assertThatThrownBy(() -> deleteSubjectTaskV2Service.execute(command))
            .isInstanceOf(TaskV2HasActiveSessionException.class);
    }
}
