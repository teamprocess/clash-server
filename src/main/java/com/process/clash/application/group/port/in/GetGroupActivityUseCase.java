package com.process.clash.application.group.port.in;

import com.process.clash.application.group.data.GetGroupActivityData;

public interface GetGroupActivityUseCase {
    GetGroupActivityData.Result execute(GetGroupActivityData.Command command);
}
