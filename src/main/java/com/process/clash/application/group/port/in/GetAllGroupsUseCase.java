package com.process.clash.application.group.port.in;

import com.process.clash.application.group.data.GetAllGroupsData;

public interface GetAllGroupsUseCase {
    GetAllGroupsData.Result execute(GetAllGroupsData.Command command);
}
