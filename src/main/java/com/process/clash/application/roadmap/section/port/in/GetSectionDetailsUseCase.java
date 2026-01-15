package com.process.clash.application.roadmap.section.port.in;

import com.process.clash.application.roadmap.section.data.GetSectionDetailsData;

public interface GetSectionDetailsUseCase {
    GetSectionDetailsData.Result execute(GetSectionDetailsData.Command command);
}
