package com.process.clash.application.roadmap.section.service;

import com.process.clash.application.roadmap.section.data.GetSectionPreviewData;
import com.process.clash.application.roadmap.section.port.in.GetSectionPreviewUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetSectionPreviewService implements GetSectionPreviewUseCase {

    @Override
    public GetSectionPreviewData.Result execute(GetSectionPreviewData.Command command) {
        return null;
    }
}
