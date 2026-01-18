package com.process.clash.application.compete.rival.service;

import com.process.clash.application.compete.rival.data.ApplyRivalData;
import com.process.clash.application.compete.rival.policy.ApplyRivalPolicy;
import com.process.clash.application.compete.rival.port.in.ApplyRivalUseCase;
import com.process.clash.application.compete.rival.port.out.RivalRepositoryPort;
import com.process.clash.domain.rival.entity.Rival;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplyRivalService implements ApplyRivalUseCase {

    private final ApplyRivalPolicy applyRivalPolicy;
    private final RivalRepositoryPort rivalRepositoryPort;

    @Override
    @Transactional
    public void execute(ApplyRivalData.Command command) {

        applyRivalPolicy.check(command);

        List<Rival> rivals = command.ids().stream()
                .map(opponentId -> Rival.createDefault(command.actor().id(), opponentId.id()))
                .toList();

        rivalRepositoryPort.saveAll(rivals);
    }
}