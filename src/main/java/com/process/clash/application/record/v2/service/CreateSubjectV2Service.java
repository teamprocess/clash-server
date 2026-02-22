package com.process.clash.application.record.v2.service;

import com.process.clash.application.record.v2.data.CreateSubjectV2Data;
import com.process.clash.application.record.v2.port.in.CreateSubjectV2UseCase;
import com.process.clash.application.record.v2.port.out.RecordSubjectV2RepositoryPort;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.record.v2.entity.RecordSubjectV2;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateSubjectV2Service implements CreateSubjectV2UseCase {

    private final RecordSubjectV2RepositoryPort recordSubjectV2RepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public void execute(CreateSubjectV2Data.Command command) {
        userRepositoryPort.findById(command.actor().id())
            .orElseThrow(UserNotFoundException::new);

        RecordSubjectV2 subject = RecordSubjectV2.create(command.name(), command.actor().id());
        recordSubjectV2RepositoryPort.save(subject);
    }
}
