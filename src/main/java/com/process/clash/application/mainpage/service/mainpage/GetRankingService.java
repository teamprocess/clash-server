package com.process.clash.application.mainpage.service.mainpage;

import com.process.clash.application.ranking.data.GetRankingData;
import com.process.clash.application.ranking.port.in.GetRankingUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetRankingService implements GetRankingUseCase {

    @Override
    public GetRankingData.Result execute(GetRankingData.Command command) {

        return null;
    }
}
