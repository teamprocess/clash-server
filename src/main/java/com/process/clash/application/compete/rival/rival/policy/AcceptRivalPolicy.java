package com.process.clash.application.compete.rival.rival.policy;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.rival.exception.exception.badrequet.TooMuchRivalsException;
import com.process.clash.application.compete.rival.rival.exception.exception.forbidden.AcceptRivalForbiddenException;
import com.process.clash.application.compete.rival.rival.exception.exception.notfound.RivalNotFoundException;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.domain.rival.rival.entity.Rival;
import com.process.clash.domain.rival.rival.enums.RivalLinkingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AcceptRivalPolicy {

    private final RivalRepositoryPort rivalRepositoryPort;
    private static final int MAX_RIVAL_COUNT = 4;

    public Rival check(Actor actor, Long id) {

        if (rivalRepositoryPort.countAllByUserId(actor.id()) >= MAX_RIVAL_COUNT)
            throw new TooMuchRivalsException();

        Long opponentId = rivalRepositoryPort.findOpponentIdByIdAndUserIdInRejectCase(id, actor.id());

        if (opponentId == null)
            throw new RivalNotFoundException();

        if (rivalRepositoryPort.countAllByUserId(opponentId) >= MAX_RIVAL_COUNT)
            throw new TooMuchRivalsException();

        Rival rival = rivalRepositoryPort.findById(id)
                .orElseThrow(RivalNotFoundException::new);

        if (rival.rivalLinkingStatus() != RivalLinkingStatus.PENDING)
            throw new RivalNotFoundException();

        if (!rival.secondUserId().equals(actor.id()))
            throw new AcceptRivalForbiddenException();

        return rival;
    }
}