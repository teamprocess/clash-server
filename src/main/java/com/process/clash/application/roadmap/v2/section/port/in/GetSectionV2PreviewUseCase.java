package com.process.clash.application.roadmap.v2.section.port.in;

import com.process.clash.application.roadmap.v2.section.data.GetSectionV2PreviewData;

public interface GetSectionV2PreviewUseCase {
    GetSectionV2PreviewData.Result execute(GetSectionV2PreviewData.Command command);
}