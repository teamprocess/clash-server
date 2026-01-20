package com.process.clash.application.compete.rival.rival.service;

import com.process.clash.application.compete.rival.rival.data.ModifyRivalData;
import com.process.clash.application.compete.rival.rival.exception.exception.notfound.RivalNotFoundException;
import com.process.clash.application.compete.rival.rival.policy.ModifyRivalPolicy;
import com.process.clash.application.compete.rival.rival.port.in.AcceptRivalUseCase;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.domain.rival.rival.entity.Rival;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AcceptRivalService implements AcceptRivalUseCase {

    private final RivalRepositoryPort rivalRepositoryPort;
    private final ModifyRivalPolicy modifyRivalPolicy;

    @Override
    public void execute(ModifyRivalData.Command command) {

        modifyRivalPolicy.check(command.actor());

        Rival rival = rivalRepositoryPort.findById(command.id())
                .orElseThrow(RivalNotFoundException::new);

        Rival updatedRival = rival.accept();

        rivalRepositoryPort.save(updatedRival);
    }
}
