package com.process.clash.application.record.v2.service;

import com.process.clash.application.record.v2.data.UpdateSubjectTaskV2Data;
import com.process.clash.application.record.v2.exception.exception.notfound.SubjectV2NotFoundException;
import com.process.clash.application.record.v2.exception.exception.notfound.TaskV2NotFoundException;
import com.process.clash.application.record.v2.policy.SubjectV2Policy;
import com.process.clash.application.record.v2.port.in.UpdateSubjectTaskV2UseCase;
import com.process.clash.application.record.v2.port.out.RecordSubjectV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordTaskV2RepositoryPort;
import com.process.clash.domain.record.v2.entity.RecordSubjectV2;
import com.process.clash.domain.record.v2.entity.RecordTaskV2;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateSubjectTaskV2Service implements UpdateSubjectTaskV2UseCase {

    private final RecordSubjectV2RepositoryPort recordSubjectV2RepositoryPort;
    private final RecordTaskV2RepositoryPort recordTaskV2RepositoryPort;
    private final SubjectV2Policy subjectV2Policy;

    @Override
    public UpdateSubjectTaskV2Data.Result execute(UpdateSubjectTaskV2Data.Command command) {
        RecordSubjectV2 subject = recordSubjectV2RepositoryPort.findById(command.subjectId())
            .orElseThrow(SubjectV2NotFoundException::new);
        subjectV2Policy.validateOwnership(command.actor(), subject);

        RecordTaskV2 task = recordTaskV2RepositoryPort.findByIdAndSubjectId(command.taskId(), subject.id())
            .orElseThrow(TaskV2NotFoundException::new);
        RecordTaskV2 updatedTask = task.changeName(command.name());
        RecordTaskV2 savedTask = recordTaskV2RepositoryPort.save(updatedTask);

        return UpdateSubjectTaskV2Data.Result.from(savedTask);
    }
}
