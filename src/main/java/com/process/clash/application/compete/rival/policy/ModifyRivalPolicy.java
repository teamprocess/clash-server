package com.process.clash.application.compete.rival.policy;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.data.ApplyRivalData;
import com.process.clash.application.compete.rival.exception.exception.badrequet.TooMuchRivalsException;
import com.process.clash.application.compete.rival.port.out.RivalRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ModifyRivalPolicy {

    private final RivalRepositoryPort rivalRepositoryPort;
    private static final int MAX_RIVAL_COUNT = 4;

    public void check(Actor actor) {

        if (rivalRepositoryPort.countAllByUserId(actor.id()) >= MAX_RIVAL_COUNT)
            throw new TooMuchRivalsException();
    }
}