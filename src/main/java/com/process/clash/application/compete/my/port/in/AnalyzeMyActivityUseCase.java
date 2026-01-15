package com.process.clash.application.compete.my.port.in;

import com.process.clash.application.compete.my.data.AnalyzeMyActivityData;

public interface AnalyzeMyActivityUseCase {

    AnalyzeMyActivityData.Result execute(AnalyzeMyActivityData.Command command);
}
