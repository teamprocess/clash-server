package com.process.clash.application.record.v2.service;

import com.process.clash.application.record.v2.data.DeleteSubjectTaskV2Data;
import com.process.clash.application.record.v2.exception.exception.conflict.TaskV2HasActiveSessionException;
import com.process.clash.application.record.v2.exception.exception.notfound.SubjectV2NotFoundException;
import com.process.clash.application.record.v2.exception.exception.notfound.TaskV2NotFoundException;
import com.process.clash.application.record.v2.policy.SubjectV2Policy;
import com.process.clash.application.record.v2.port.in.DeleteSubjectTaskV2UseCase;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordSubjectV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordTaskV2RepositoryPort;
import com.process.clash.domain.record.v2.entity.RecordSubjectV2;
import com.process.clash.domain.record.v2.entity.RecordTaskV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteSubjectTaskV2Service implements DeleteSubjectTaskV2UseCase {

    private final RecordSubjectV2RepositoryPort recordSubjectV2RepositoryPort;
    private final RecordTaskV2RepositoryPort recordTaskV2RepositoryPort;
    private final RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;
    private final SubjectV2Policy subjectV2Policy;

    @Override
    public void execute(DeleteSubjectTaskV2Data.Command command) {
        RecordSubjectV2 subject = recordSubjectV2RepositoryPort.findById(command.subjectId())
            .orElseThrow(SubjectV2NotFoundException::new);
        subjectV2Policy.validateOwnership(command.actor(), subject);

        RecordTaskV2 task = recordTaskV2RepositoryPort.findByIdAndSubjectId(command.taskId(), subject.id())
            .orElseThrow(TaskV2NotFoundException::new);

        if (recordSessionV2RepositoryPort.existsActiveSessionByTaskId(task.id())) {
            throw new TaskV2HasActiveSessionException();
        }
        recordTaskV2RepositoryPort.deleteById(task.id());
    }
}
