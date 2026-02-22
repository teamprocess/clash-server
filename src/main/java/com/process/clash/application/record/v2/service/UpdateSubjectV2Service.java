package com.process.clash.application.record.v2.service;

import com.process.clash.application.record.v2.data.UpdateSubjectV2Data;
import com.process.clash.application.record.v2.exception.exception.notfound.SubjectV2NotFoundException;
import com.process.clash.application.record.v2.policy.SubjectV2Policy;
import com.process.clash.application.record.v2.port.in.UpdateSubjectV2UseCase;
import com.process.clash.application.record.v2.port.out.RecordSubjectV2RepositoryPort;
import com.process.clash.domain.record.v2.entity.RecordSubjectV2;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateSubjectV2Service implements UpdateSubjectV2UseCase {

    private final RecordSubjectV2RepositoryPort recordSubjectV2RepositoryPort;
    private final SubjectV2Policy subjectV2Policy;

    @Override
    public UpdateSubjectV2Data.Result execute(UpdateSubjectV2Data.Command command) {
        RecordSubjectV2 subject = recordSubjectV2RepositoryPort.findById(command.subjectId())
            .orElseThrow(SubjectV2NotFoundException::new);
        subjectV2Policy.validateOwnership(command.actor(), subject);

        RecordSubjectV2 updatedSubject = subject.changeName(command.name());
        RecordSubjectV2 savedSubject = recordSubjectV2RepositoryPort.save(updatedSubject);

        return UpdateSubjectV2Data.Result.from(savedSubject);
    }
}
