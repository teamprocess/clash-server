package com.process.clash.application.compete.my.port.in;

import com.process.clash.application.compete.my.data.AnalyzeMyPeriodData;

public interface AnalyzeMyPeriodUseCase {

    AnalyzeMyPeriodData.Result execute(AnalyzeMyPeriodData.Command command);
}