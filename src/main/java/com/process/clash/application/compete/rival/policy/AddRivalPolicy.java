package com.process.clash.application.compete.rival.policy;

import com.process.clash.application.compete.rival.data.AddRivalData;
import com.process.clash.application.compete.rival.exception.exception.TooMuchRivalsException;
import com.process.clash.application.compete.rival.port.out.RivalRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddRivalPolicy {

    private final RivalRepositoryPort rivalRepositoryPort;

    public void check(AddRivalData.Command command) {

        int myRivalCount = rivalRepositoryPort.countAllByMy_Id(command.actor().id());

        if (myRivalCount + command.ids().toArray().length > 4)
            throw new TooMuchRivalsException();
    }
}
