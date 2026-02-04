package com.process.clash.application.roadmap.v2.choice.port.in;

import com.process.clash.application.roadmap.v2.choice.data.UpdateChoiceV2Data;

public interface UpdateChoiceV2UseCase {
    UpdateChoiceV2Data.Result execute(UpdateChoiceV2Data.Command command);
}
