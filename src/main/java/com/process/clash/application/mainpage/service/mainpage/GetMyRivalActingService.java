package com.process.clash.application.mainpage.service.mainpage;

import com.process.clash.application.mainpage.data.mainpage.GetMyRivalActingData;
import com.process.clash.application.mainpage.port.in.mainpage.GetMyRivalActingUseCase;
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
