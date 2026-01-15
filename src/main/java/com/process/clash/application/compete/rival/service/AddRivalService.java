package com.process.clash.application.compete.rival.service;

import com.process.clash.application.compete.rival.data.AddRivalData;
import com.process.clash.application.compete.rival.policy.AddRivalPolicy;
import com.process.clash.application.compete.rival.port.in.AddRivalUseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddRivalService implements AddRivalUseCase {

    private final AddRivalPolicy addRivalPolicy;

    @Override
    @Transactional
    public void execute(AddRivalData.Command command) {

        addRivalPolicy.check(command);


    }
}
