package com.process.clash.application.compete.rival.rival.service;

import com.process.clash.application.compete.realtime.CompeteRefetchNotifier;
import com.process.clash.application.compete.rival.rival.data.ModifyRivalData;
import com.process.clash.application.compete.rival.rival.exception.exception.notfound.RivalNotFoundException;
import com.process.clash.application.compete.rival.rival.policy.RemoveRivalPolicy;
import com.process.clash.application.compete.rival.rival.port.in.RemoveRivalUseCase;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.domain.rival.rival.entity.Rival;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RemoveRivalService implements RemoveRivalUseCase {

    private final RemoveRivalPolicy removeRivalPolicy;
    private final RivalRepositoryPort rivalRepositoryPort;
    private final CompeteRefetchNotifier competeRefetchNotifier;

    @Override
    public void execute(ModifyRivalData.Command command) {

        Rival rival = removeRivalPolicy.check(command.id());

        if (!rival.firstUserId().equals(command.actor().id()) && !rival.secondUserId().equals(command.actor().id())) {
            throw new RivalNotFoundException();
        }

        rivalRepositoryPort.deleteById(rival.id());
        competeRefetchNotifier.notifyCompeteChanged(List.of(rival.firstUserId(), rival.secondUserId()));
    }
}
