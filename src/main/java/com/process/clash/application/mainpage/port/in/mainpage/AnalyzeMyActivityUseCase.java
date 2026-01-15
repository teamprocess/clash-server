package com.process.clash.application.mainpage.port.in.mainpage;

import com.process.clash.application.compete.my.data.AnalyzeMyActivityData;

public interface AnalyzeMyActivityUseCase {

    AnalyzeMyActivityData.Result execute(AnalyzeMyActivityData.Command command);
}
