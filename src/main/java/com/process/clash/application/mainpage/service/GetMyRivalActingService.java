package com.process.clash.application.mainpage.service;

import com.process.clash.application.mainpage.data.GetMyRivalActingData;
import com.process.clash.application.mainpage.port.in.GetMyRivalActingUseCase;
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
