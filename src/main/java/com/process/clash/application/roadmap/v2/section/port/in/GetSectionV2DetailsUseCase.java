package com.process.clash.application.roadmap.v2.section.port.in;

import com.process.clash.application.roadmap.v2.section.data.GetSectionV2DetailsData;

public interface GetSectionV2DetailsUseCase {
    GetSectionV2DetailsData.Result execute(GetSectionV2DetailsData.Command command);
}
