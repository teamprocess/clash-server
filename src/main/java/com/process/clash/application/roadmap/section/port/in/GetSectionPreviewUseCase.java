package com.process.clash.application.roadmap.section.port.in;

import com.process.clash.application.roadmap.section.data.GetSectionPreviewData;

public interface GetSectionPreviewUseCase {
    GetSectionPreviewData.Result execute(GetSectionPreviewData.Command command);
}
