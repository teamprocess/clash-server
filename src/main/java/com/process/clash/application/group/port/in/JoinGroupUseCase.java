package com.process.clash.application.group.port.in;

import com.process.clash.application.group.data.JoinGroupData;

public interface JoinGroupUseCase {
    void execute(JoinGroupData.Command command);
}
