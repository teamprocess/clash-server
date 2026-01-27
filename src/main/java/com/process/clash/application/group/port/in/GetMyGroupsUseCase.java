package com.process.clash.application.group.port.in;

import com.process.clash.application.group.data.GetMyGroupsData;

public interface GetMyGroupsUseCase {
    GetMyGroupsData.Result execute(GetMyGroupsData.Command command);
}
