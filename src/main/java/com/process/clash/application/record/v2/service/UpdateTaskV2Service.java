package com.process.clash.application.record.v2.service;

import com.process.clash.application.record.v2.data.UpdateTaskV2Data;
import com.process.clash.application.record.v2.exception.exception.notfound.SubjectV2NotFoundException;
import com.process.clash.application.record.v2.exception.exception.notfound.TaskV2NotFoundException;
import com.process.clash.application.record.v2.policy.SubjectV2Policy;
import com.process.clash.application.record.v2.port.in.UpdateTaskV2UseCase;
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
public class UpdateTaskV2Service implements UpdateTaskV2UseCase {

    private final RecordSubjectV2RepositoryPort recordSubjectV2RepositoryPort;
    private final RecordTaskV2RepositoryPort recordTaskV2RepositoryPort;
    private final SubjectV2Policy subjectV2Policy;

    @Override
    public UpdateTaskV2Data.Result execute(UpdateTaskV2Data.Command command) {
        RecordTaskV2 task = recordTaskV2RepositoryPort.findByIdAndUserId(command.taskId(), command.actor().id())
            .orElseThrow(TaskV2NotFoundException::new);

        Long subjectId = command.subjectId();
        if (subjectId != null) {
            RecordSubjectV2 subject = recordSubjectV2RepositoryPort.findById(subjectId)
                .orElseThrow(SubjectV2NotFoundException::new);
            subjectV2Policy.validateOwnership(command.actor(), subject);
        }

        RecordTaskV2 updatedTask = task.changeDetails(command.name(), subjectId);
        RecordTaskV2 savedTask = recordTaskV2RepositoryPort.save(updatedTask);

        return UpdateTaskV2Data.Result.from(savedTask);
    }
}
