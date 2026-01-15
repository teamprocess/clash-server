package com.process.clash.application.compete.rival.service;

import com.process.clash.application.compete.rival.data.ApplyRivalData;
import com.process.clash.application.compete.rival.policy.ApplyRivalPolicy;
import com.process.clash.application.compete.rival.port.in.ApplyRivalUseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplyRivalService implements ApplyRivalUseCase {

    private final ApplyRivalPolicy applyRivalPolicy;

    @Override
    @Transactional
    public void execute(ApplyRivalData.Command command) {

        applyRivalPolicy.check(command);


    }
}
