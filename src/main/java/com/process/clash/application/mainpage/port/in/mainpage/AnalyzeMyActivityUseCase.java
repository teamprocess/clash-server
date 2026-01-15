package com.process.clash.application.mainpage.port.in.mainpage;

import com.process.clash.application.mainpage.data.mainpage.AnalyzeMyActivityData;

public interface AnalyzeMyActivityUseCase {

    AnalyzeMyActivityData.Result execute(AnalyzeMyActivityData.Command command);
}
