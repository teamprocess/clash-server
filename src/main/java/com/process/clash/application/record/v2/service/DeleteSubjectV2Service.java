package com.process.clash.application.record.v2.service;

import com.process.clash.application.record.v2.data.DeleteSubjectV2Data;
import com.process.clash.application.record.v2.exception.exception.conflict.SubjectV2HasActiveSessionException;
import com.process.clash.application.record.v2.exception.exception.notfound.SubjectV2NotFoundException;
import com.process.clash.application.record.v2.policy.SubjectV2Policy;
import com.process.clash.application.record.v2.port.in.DeleteSubjectV2UseCase;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordSubjectV2RepositoryPort;
import com.process.clash.domain.record.v2.entity.RecordSubjectV2;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteSubjectV2Service implements DeleteSubjectV2UseCase {

    private final RecordSubjectV2RepositoryPort recordSubjectV2RepositoryPort;
    private final RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;
    private final SubjectV2Policy subjectV2Policy;

    @Override
    public void execute(DeleteSubjectV2Data.Command command) {
        RecordSubjectV2 subject = recordSubjectV2RepositoryPort.findById(command.subjectId())
            .orElseThrow(SubjectV2NotFoundException::new);
        subjectV2Policy.validateOwnership(command.actor(), subject);

        if (recordSessionV2RepositoryPort.existsActiveSessionBySubjectId(subject.id())) {
            throw new SubjectV2HasActiveSessionException();
        }
        try {
            recordSubjectV2RepositoryPort.deleteById(subject.id());
        } catch (DataIntegrityViolationException exception) {
            throw new SubjectV2HasActiveSessionException(exception);
        }
    }
}
