package com.process.clash.application.record.v2.service;

import com.process.clash.application.record.v2.data.CreateSubjectTaskV2Data;
import com.process.clash.application.record.v2.exception.exception.notfound.SubjectV2NotFoundException;
import com.process.clash.application.record.v2.policy.SubjectV2Policy;
import com.process.clash.application.record.v2.port.in.CreateSubjectTaskV2UseCase;
import com.process.clash.application.record.v2.port.out.RecordSubjectV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordTaskV2RepositoryPort;
import com.process.clash.application.record.v2.util.RecordDayWindow;
import com.process.clash.domain.record.v2.entity.RecordSubjectV2;
import com.process.clash.domain.record.v2.entity.RecordTaskV2;
import com.process.clash.infrastructure.config.record.RecordProperties;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateSubjectTaskV2Service implements CreateSubjectTaskV2UseCase {

    private final RecordSubjectV2RepositoryPort recordSubjectV2RepositoryPort;
    private final RecordTaskV2RepositoryPort recordTaskV2RepositoryPort;
    private final SubjectV2Policy subjectV2Policy;
    private final RecordProperties recordProperties;
    private final ZoneId recordZoneId;

    @Override
    public void execute(CreateSubjectTaskV2Data.Command command) {
        Long subjectId = command.subjectId();
        if (subjectId != null) {
            RecordSubjectV2 subject = recordSubjectV2RepositoryPort.findById(subjectId)
                .orElseThrow(SubjectV2NotFoundException::new);
            subjectV2Policy.validateOwnership(command.actor(), subject);
        }

        LocalDate recordDate = command.date() == null
            ? RecordDayWindow.today(recordZoneId, recordProperties.dayBoundaryHour()).recordDate()
            : command.date();

        RecordTaskV2 task = RecordTaskV2.create(command.name(), command.actor().id(), subjectId, recordDate);
        recordTaskV2RepositoryPort.save(task);
    }
}
