package com.process.clash.application.group.port.in;

import com.process.clash.application.group.data.QuitGroupData;

public interface QuitGroupUseCase {
    void execute(QuitGroupData.Command command);
}
