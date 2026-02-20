package com.process.clash.application.compete.rival.rival.service;

import com.process.clash.application.compete.rival.rival.data.ModifyRivalData;
import com.process.clash.application.compete.rival.rival.policy.RemoveRivalPolicy;
import com.process.clash.application.compete.rival.rival.port.in.RemoveRivalUseCase;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.domain.rival.rival.entity.Rival;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RemoveRivalService implements RemoveRivalUseCase {

    private final RemoveRivalPolicy removeRivalPolicy;
    private final RivalRepositoryPort rivalRepositoryPort;

    @Override
    public void execute(ModifyRivalData.Command command) {

        Rival rival = removeRivalPolicy.check(command.id());

        rivalRepositoryPort.deleteById(rival.id());
    }
}
