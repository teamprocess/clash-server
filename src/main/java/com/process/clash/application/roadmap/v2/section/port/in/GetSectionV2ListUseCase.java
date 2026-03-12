package com.process.clash.application.roadmap.v2.section.port.in;

import com.process.clash.application.roadmap.v2.section.data.GetSectionV2ListData;

public interface GetSectionV2ListUseCase {
    GetSectionV2ListData.Result execute(GetSectionV2ListData.Command command);
}
