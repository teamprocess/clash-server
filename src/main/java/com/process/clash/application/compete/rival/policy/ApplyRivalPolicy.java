package com.process.clash.application.compete.rival.policy;

import com.process.clash.application.compete.rival.data.ApplyRivalData;
import com.process.clash.application.compete.rival.exception.exception.TooMuchRivalsException;
import com.process.clash.application.compete.rival.port.out.RivalRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplyRivalPolicy {

    private final RivalRepositoryPort rivalRepositoryPort;
    private static final int MAX_RIVAL_COUNT = 4;

    public void check(ApplyRivalData.Command command) {

        int myRivalCount = rivalRepositoryPort.countAllByMyId(command.actor().id());

        if (myRivalCount + command.ids().size() > MAX_RIVAL_COUNT) {
            throw new TooMuchRivalsException();
        }

        boolean hasOverLimitRival = command.ids().stream()
                .anyMatch(id -> rivalRepositoryPort.countAllByOpponentId(id.id()) >= MAX_RIVAL_COUNT);

        if (hasOverLimitRival) {
            throw new TooMuchRivalsException();
        }
    }
}
