package com.process.clash.application.group.port.in;

import com.process.clash.application.group.data.CreateGroupData;

public interface CreateGroupUseCase {
    void execute(CreateGroupData.Command command);
}
