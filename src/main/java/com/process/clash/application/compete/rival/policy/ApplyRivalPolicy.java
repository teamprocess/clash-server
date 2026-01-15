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

    public void check(ApplyRivalData.Command command) {

        int myRivalCount = rivalRepositoryPort.countAllByMy_Id(command.actor().id());

        if (myRivalCount + command.ids().size() > 4) {
            throw new TooMuchRivalsException();
        }

        boolean hasOverLimitRival = command.ids().stream()
                .anyMatch(id -> rivalRepositoryPort.countAllByOpponent_Id(id.id()) >= 4);

        if (hasOverLimitRival) {
            throw new TooMuchRivalsException();
        }
    }
}
