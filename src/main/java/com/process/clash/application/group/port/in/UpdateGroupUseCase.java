package com.process.clash.application.group.port.in;

import com.process.clash.application.group.data.UpdateGroupData;

public interface UpdateGroupUseCase {
    void execute(UpdateGroupData.Command command);
}
