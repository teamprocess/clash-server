package com.process.clash.application.record.v2.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.v2.data.UpdateTaskV2Data;
import com.process.clash.application.record.v2.exception.exception.forbidden.SubjectV2AccessDeniedException;
import com.process.clash.application.record.v2.exception.exception.notfound.SubjectV2NotFoundException;
import com.process.clash.application.record.v2.exception.exception.notfound.TaskV2NotFoundException;
import com.process.clash.application.record.v2.policy.SubjectV2Policy;
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

@ExtendWith(MockitoExtension.class)
class UpdateTaskV2ServiceTest {

    @Mock
    private RecordSubjectV2RepositoryPort recordSubjectV2RepositoryPort;

    @Mock
    private RecordTaskV2RepositoryPort recordTaskV2RepositoryPort;

    private UpdateTaskV2Service updateTaskV2Service;

    @BeforeEach
    void setUp() {
        updateTaskV2Service = new UpdateTaskV2Service(
            recordSubjectV2RepositoryPort,
            recordTaskV2RepositoryPort,
            new SubjectV2Policy()
        );
    }

    @Test
    @DisplayName("부모 없는 세부 작업도 이름과 부모 과목을 수정할 수 있다")
    void execute_updatesUngroupedTaskAndMovesSubject() {
        Actor actor = new Actor(1L);
        RecordSubjectV2 subject = new RecordSubjectV2(10L, 1L, "백엔드", 0L, Instant.now(), Instant.now());
        RecordTaskV2 task = new RecordTaskV2(11L, 1L, null, "리팩터링", false, 0L, Instant.now(), Instant.now());
        UpdateTaskV2Data.Command command = new UpdateTaskV2Data.Command(actor, 11L, 10L, "리팩터링 정리");

        when(recordTaskV2RepositoryPort.findByIdAndUserId(11L, 1L)).thenReturn(Optional.of(task));
        when(recordSubjectV2RepositoryPort.findById(10L)).thenReturn(Optional.of(subject));
        when(recordTaskV2RepositoryPort.save(any(RecordTaskV2.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        UpdateTaskV2Data.Result result = updateTaskV2Service.execute(command);

        assertThat(result.task().name()).isEqualTo("리팩터링 정리");
        assertThat(result.task().subjectId()).isEqualTo(10L);
        verify(recordTaskV2RepositoryPort).save(any(RecordTaskV2.class));
    }

    @Test
    @DisplayName("task가 없으면 예외가 발생한다")
    void execute_throwsWhenTaskNotFound() {
        Actor actor = new Actor(1L);
        UpdateTaskV2Data.Command command = new UpdateTaskV2Data.Command(actor, 11L, null, "리팩터링 정리");

        when(recordTaskV2RepositoryPort.findByIdAndUserId(11L, 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateTaskV2Service.execute(command))
            .isInstanceOf(TaskV2NotFoundException.class);
    }

    @Test
    @DisplayName("이동할 과목이 없으면 예외가 발생한다")
    void execute_throwsWhenSubjectNotFound() {
        Actor actor = new Actor(1L);
        RecordTaskV2 task = new RecordTaskV2(11L, 1L, null, "리팩터링", false, 0L, Instant.now(), Instant.now());
        UpdateTaskV2Data.Command command = new UpdateTaskV2Data.Command(actor, 11L, 10L, "리팩터링 정리");

        when(recordTaskV2RepositoryPort.findByIdAndUserId(11L, 1L)).thenReturn(Optional.of(task));
        when(recordSubjectV2RepositoryPort.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateTaskV2Service.execute(command))
            .isInstanceOf(SubjectV2NotFoundException.class);
    }

    @Test
    @DisplayName("본인 과목이 아니면 다른 과목으로 이동할 수 없다")
    void execute_throwsWhenTargetSubjectIsNotOwned() {
        Actor actor = new Actor(1L);
        RecordSubjectV2 subject = new RecordSubjectV2(10L, 2L, "백엔드", 0L, Instant.now(), Instant.now());
        RecordTaskV2 task = new RecordTaskV2(11L, 1L, null, "리팩터링", false, 0L, Instant.now(), Instant.now());
        UpdateTaskV2Data.Command command = new UpdateTaskV2Data.Command(actor, 11L, 10L, "리팩터링 정리");

        when(recordTaskV2RepositoryPort.findByIdAndUserId(11L, 1L)).thenReturn(Optional.of(task));
        when(recordSubjectV2RepositoryPort.findById(10L)).thenReturn(Optional.of(subject));

        assertThatThrownBy(() -> updateTaskV2Service.execute(command))
            .isInstanceOf(SubjectV2AccessDeniedException.class);
    }
}
