package com.process.clash.application.record.v2.port.in;

import com.process.clash.application.record.v2.data.SwitchDevelopAppV2Data;

public interface SwitchDevelopAppV2UseCase {

    SwitchDevelopAppV2Data.Result execute(SwitchDevelopAppV2Data.Command command);
}
