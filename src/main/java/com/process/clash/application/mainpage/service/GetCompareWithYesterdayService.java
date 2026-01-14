package com.process.clash.application.mainpage.service;

import com.process.clash.application.mainpage.data.GetCompareWithYesterdayData;
import com.process.clash.application.mainpage.port.in.GetCompareWithYesterdayUseCase;
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
