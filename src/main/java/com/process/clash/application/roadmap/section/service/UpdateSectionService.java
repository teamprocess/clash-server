package com.process.clash.application.roadmap.section.service;

import com.process.clash.application.roadmap.section.data.UpdateSectionData;
import com.process.clash.application.roadmap.section.port.in.UpdateSectionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateSectionService implements UpdateSectionUseCase {

    @Override
    public UpdateSectionData.Result execute(UpdateSectionData.Command command) {
        return null;
    }
}
