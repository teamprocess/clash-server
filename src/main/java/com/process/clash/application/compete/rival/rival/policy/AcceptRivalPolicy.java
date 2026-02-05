package com.process.clash.application.compete.rival.rival.policy;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.rival.exception.exception.badrequet.TooMuchRivalsException;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AcceptRivalPolicy {

    private final RivalRepositoryPort rivalRepositoryPort;
    private static final int MAX_RIVAL_COUNT = 4;

    public void check(Actor actor, Long id) {

        if (rivalRepositoryPort.countAllByUserId(actor.id()) >= MAX_RIVAL_COUNT)
            throw new TooMuchRivalsException();

        Long rivalId = rivalRepositoryPort.findOpponentIdByIdAndUserId(id, actor.id());

        if (rivalRepositoryPort.countAllByUserId(rivalId) >= MAX_RIVAL_COUNT)
            throw new TooMuchRivalsException();
    }
}