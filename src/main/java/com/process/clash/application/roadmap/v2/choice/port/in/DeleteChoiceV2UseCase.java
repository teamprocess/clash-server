package com.process.clash.application.roadmap.v2.choice.port.in;

import com.process.clash.application.roadmap.v2.choice.data.DeleteChoiceV2Data;

public interface DeleteChoiceV2UseCase {
    void execute(DeleteChoiceV2Data.Command command);
}
