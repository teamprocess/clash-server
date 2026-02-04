package com.process.clash.application.roadmap.v2.choice.port.in;

import com.process.clash.application.roadmap.v2.choice.data.CreateChoiceV2Data;

public interface CreateChoiceV2UseCase {
    CreateChoiceV2Data.Result execute(CreateChoiceV2Data.Command command);
}
