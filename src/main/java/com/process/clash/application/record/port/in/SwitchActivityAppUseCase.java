package com.process.clash.application.record.port.in;

import com.process.clash.application.record.data.SwitchActivityAppData;

public interface SwitchActivityAppUseCase {

    SwitchActivityAppData.Result execute(SwitchActivityAppData.Command command);
}
