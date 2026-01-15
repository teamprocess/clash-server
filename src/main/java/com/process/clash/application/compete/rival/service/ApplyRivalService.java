package com.process.clash.application.compete.rival.service;

import com.process.clash.application.compete.rival.data.ApplyRivalData;
import com.process.clash.application.compete.rival.policy.ApplyRivalPolicy;
import com.process.clash.application.compete.rival.port.in.ApplyRivalUseCase;
import com.process.clash.application.compete.rival.port.out.RivalRepositoryPort;
import com.process.clash.domain.rival.entity.Rival;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplyRivalService implements ApplyRivalUseCase {

    private final ApplyRivalPolicy applyRivalPolicy;
    private final RivalRepositoryPort rivalRepositoryPort;

    @Override
    @Transactional
    public void execute(ApplyRivalData.Command command) {

        applyRivalPolicy.check(command);

        for (int i = 0; i < command.ids().size(); i++) {
            Rival rival = Rival.createDefault(command.actor().id(), command.ids().get(i).id());

            rivalRepositoryPort.save(rival);
        }
    }
}
