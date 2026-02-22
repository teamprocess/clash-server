package com.process.clash.application.record.v2.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.v2.data.UpdateSubjectV2Data;
import com.process.clash.application.record.v2.exception.exception.forbidden.SubjectV2AccessDeniedException;
import com.process.clash.application.record.v2.exception.exception.notfound.SubjectV2NotFoundException;
import com.process.clash.application.record.v2.policy.SubjectV2Policy;
import com.process.clash.application.record.v2.port.out.RecordSubjectV2RepositoryPort;
import com.process.clash.domain.record.v2.entity.RecordSubjectV2;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateSubjectV2ServiceTest {

    @Mock
    private RecordSubjectV2RepositoryPort recordSubjectV2RepositoryPort;

    private UpdateSubjectV2Service updateSubjectV2Service;

    @BeforeEach
    void setUp() {
        updateSubjectV2Service = new UpdateSubjectV2Service(
            recordSubjectV2RepositoryPort,
            new SubjectV2Policy()
        );
    }

    @Test
    @DisplayName("본인 과목 그룹이면 이름을 수정한다")
    void execute_updatesSubjectName() {
        Actor actor = new Actor(1L);
        RecordSubjectV2 subject = new RecordSubjectV2(10L, 1L, "기존", 0L, Instant.now(), Instant.now());
        UpdateSubjectV2Data.Command command = new UpdateSubjectV2Data.Command(actor, 10L, "변경");

        when(recordSubjectV2RepositoryPort.findById(10L)).thenReturn(Optional.of(subject));
        when(recordSubjectV2RepositoryPort.save(any(RecordSubjectV2.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        UpdateSubjectV2Data.Result result = updateSubjectV2Service.execute(command);

        assertThat(result.subject().name()).isEqualTo("변경");
        verify(recordSubjectV2RepositoryPort).save(any(RecordSubjectV2.class));
    }

    @Test
    @DisplayName("과목 그룹이 없으면 예외가 발생한다")
    void execute_throwsWhenSubjectNotFound() {
        Actor actor = new Actor(1L);
        UpdateSubjectV2Data.Command command = new UpdateSubjectV2Data.Command(actor, 10L, "변경");
        when(recordSubjectV2RepositoryPort.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateSubjectV2Service.execute(command))
            .isInstanceOf(SubjectV2NotFoundException.class);
    }

    @Test
    @DisplayName("본인 소유 과목 그룹이 아니면 예외가 발생한다")
    void execute_throwsWhenNotOwner() {
        Actor actor = new Actor(1L);
        RecordSubjectV2 subject = new RecordSubjectV2(10L, 2L, "기존", 0L, Instant.now(), Instant.now());
        UpdateSubjectV2Data.Command command = new UpdateSubjectV2Data.Command(actor, 10L, "변경");
        when(recordSubjectV2RepositoryPort.findById(10L)).thenReturn(Optional.of(subject));

        assertThatThrownBy(() -> updateSubjectV2Service.execute(command))
            .isInstanceOf(SubjectV2AccessDeniedException.class);
        verify(recordSubjectV2RepositoryPort, never()).save(any());
    }
}
