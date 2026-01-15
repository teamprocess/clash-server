package com.process.clash.application.compete.rival.service;

import com.process.clash.application.compete.rival.data.GetAllAbleRivalsData;
import com.process.clash.application.compete.rival.port.in.GetAllAbleRivalsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllAbleRivalsService implements GetAllAbleRivalsUseCase {

    @Override
    public GetAllAbleRivalsData.Result execute(GetAllAbleRivalsData.Command command) {

        return null;
    }
}
