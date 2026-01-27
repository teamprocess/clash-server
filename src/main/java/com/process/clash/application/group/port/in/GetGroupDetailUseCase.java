package com.process.clash.application.group.port.in;

import com.process.clash.application.group.data.GetGroupDetailData;

public interface GetGroupDetailUseCase {
    GetGroupDetailData.Result execute(GetGroupDetailData.Command command);
}
