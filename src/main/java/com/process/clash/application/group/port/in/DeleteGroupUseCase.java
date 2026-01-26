package com.process.clash.application.group.port.in;

import com.process.clash.application.group.data.DeleteGroupData;

public interface DeleteGroupUseCase {
    void execute(DeleteGroupData.Command command);
}
