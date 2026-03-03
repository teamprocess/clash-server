package com.process.clash.application.record.v2.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.v2.data.CreateSubjectTaskV2Data;
import com.process.clash.application.record.v2.exception.exception.forbidden.SubjectV2AccessDeniedException;
import com.process.clash.application.record.v2.exception.exception.notfound.SubjectV2NotFoundException;
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
class CreateSubjectTaskV2ServiceTest {

    @Mock
    private RecordSubjectV2RepositoryPort recordSubjectV2RepositoryPort;

    @Mock
    private RecordTaskV2RepositoryPort recordTaskV2RepositoryPort;

    private CreateSubjectTaskV2Service createSubjectTaskV2Service;

    @BeforeEach
    void setUp() {
        createSubjectTaskV2Service = new CreateSubjectTaskV2Service(
            recordSubjectV2RepositoryPort,
            recordTaskV2RepositoryPort,
            new SubjectV2Policy()
        );
    }

    @Test
    @DisplayName("본인 과목 그룹이면 세부 작업을 생성한다")
    void execute_createsTask() {
        Actor actor = new Actor(1L);
        RecordSubjectV2 subject = new RecordSubjectV2(10L, 1L, "백엔드", 0L, Instant.now(), Instant.now());
        CreateSubjectTaskV2Data.Command command = new CreateSubjectTaskV2Data.Command(actor, 10L, "ERD 설계");

        when(recordSubjectV2RepositoryPort.findById(10L)).thenReturn(Optional.of(subject));
        when(recordTaskV2RepositoryPort.save(any(RecordTaskV2.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        createSubjectTaskV2Service.execute(command);

        verify(recordTaskV2RepositoryPort).save(any(RecordTaskV2.class));
    }

    @Test
    @DisplayName("subject 없이도 세부 작업을 생성한다")
    void execute_createsTaskWithoutSubject() {
        Actor actor = new Actor(1L);
        CreateSubjectTaskV2Data.Command command = new CreateSubjectTaskV2Data.Command(actor, null, "리팩터링");

        when(recordTaskV2RepositoryPort.save(any(RecordTaskV2.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        createSubjectTaskV2Service.execute(command);

        verify(recordSubjectV2RepositoryPort, never()).findById(any());
        verify(recordTaskV2RepositoryPort).save(argThat(task ->
            task.subjectId() == null
                && task.userId().equals(1L)
                && task.name().equals("리팩터링")
        ));
    }

    @Test
    @DisplayName("과목 그룹이 없으면 예외가 발생한다")
    void execute_throwsWhenSubjectNotFound() {
        Actor actor = new Actor(1L);
        CreateSubjectTaskV2Data.Command command = new CreateSubjectTaskV2Data.Command(actor, 10L, "ERD 설계");
        when(recordSubjectV2RepositoryPort.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> createSubjectTaskV2Service.execute(command))
            .isInstanceOf(SubjectV2NotFoundException.class);
    }

    @Test
    @DisplayName("소유자가 아니면 생성할 수 없다")
    void execute_throwsWhenNotOwner() {
        Actor actor = new Actor(1L);
        RecordSubjectV2 subject = new RecordSubjectV2(10L, 2L, "백엔드", 0L, Instant.now(), Instant.now());
        CreateSubjectTaskV2Data.Command command = new CreateSubjectTaskV2Data.Command(actor, 10L, "ERD 설계");
        when(recordSubjectV2RepositoryPort.findById(10L)).thenReturn(Optional.of(subject));

        assertThatThrownBy(() -> createSubjectTaskV2Service.execute(command))
            .isInstanceOf(SubjectV2AccessDeniedException.class);
        verify(recordTaskV2RepositoryPort, never()).save(any());
    }
}
