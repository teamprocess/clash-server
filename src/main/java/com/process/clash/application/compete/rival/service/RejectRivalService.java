package com.process.clash.application.compete.rival.service;

import com.process.clash.application.compete.rival.data.ModifyRivalData;
import com.process.clash.application.compete.rival.exception.exception.notfound.RivalNotFoundException;
import com.process.clash.application.compete.rival.port.in.RejectRivalUseCase;
import com.process.clash.application.compete.rival.port.out.RivalRepositoryPort;
import com.process.clash.domain.rival.entity.Rival;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RejectRivalService implements RejectRivalUseCase {

    private final RivalRepositoryPort rivalRepositoryPort;

    @Override
    public void execute(ModifyRivalData.Command command) {

        Rival rival = rivalRepositoryPort.findById(command.id())
                .orElseThrow(RivalNotFoundException::new);

        Rival updatedRival = rival.reject();

        rivalRepositoryPort.save(updatedRival);
    }
}
