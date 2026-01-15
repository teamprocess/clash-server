package com.process.clash.application.compete.rival.service;

import com.process.clash.application.compete.rival.data.GetMyRivalActingData;
import com.process.clash.application.compete.rival.port.in.GetMyRivalActingUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetMyRivalActingService implements GetMyRivalActingUseCase {

    @Override
    public GetMyRivalActingData.Result execute(GetMyRivalActingData.Command command) {

        return null;
    }
}
