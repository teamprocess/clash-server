package com.process.clash.application.compete.my.port.in;

import com.process.clash.application.compete.my.data.CompareMyActivityData;

public interface CompareMyActivityUseCase {

    CompareMyActivityData.Result execute(CompareMyActivityData.Command command);
}
