package com.process.clash.application.record.v2.service;

import com.process.clash.application.record.v2.data.CreateSubjectTaskV2Data;
import com.process.clash.application.record.v2.exception.exception.notfound.SubjectV2NotFoundException;
import com.process.clash.application.record.v2.policy.SubjectV2Policy;
import com.process.clash.application.record.v2.port.in.CreateSubjectTaskV2UseCase;
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
public class CreateSubjectTaskV2Service implements CreateSubjectTaskV2UseCase {

    private final RecordSubjectV2RepositoryPort recordSubjectV2RepositoryPort;
    private final RecordTaskV2RepositoryPort recordTaskV2RepositoryPort;
    private final SubjectV2Policy subjectV2Policy;

    @Override
    public void execute(CreateSubjectTaskV2Data.Command command) {
        Long subjectId = command.subjectId();
        if (subjectId != null) {
            RecordSubjectV2 subject = recordSubjectV2RepositoryPort.findById(subjectId)
                .orElseThrow(SubjectV2NotFoundException::new);
            subjectV2Policy.validateOwnership(command.actor(), subject);
        }

        RecordTaskV2 task = RecordTaskV2.create(command.name(), command.actor().id(), subjectId);
        recordTaskV2RepositoryPort.save(task);
    }
}
