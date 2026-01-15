package com.process.clash.application.mainpage.service.mainpage;

import com.process.clash.application.compete.my.data.GetCompareWithYesterdayData;
import com.process.clash.application.compete.my.port.in.GetCompareWithYesterdayUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCompareWithYesterdayService implements GetCompareWithYesterdayUseCase {

    @Override
    public GetCompareWithYesterdayData.Result execute(GetCompareWithYesterdayData.Command command) {

        return null;
    }
}
