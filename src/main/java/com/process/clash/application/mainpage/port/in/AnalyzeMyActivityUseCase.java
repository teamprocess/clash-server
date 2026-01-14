package com.process.clash.application.mainpage.port.in;

import com.process.clash.application.mainpage.data.AnalyzeMyActivityData;

public interface AnalyzeMyActivityUseCase {

    AnalyzeMyActivityData.Result execute(AnalyzeMyActivityData.Command command);
}
