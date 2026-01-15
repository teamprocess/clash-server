package com.process.clash.application.roadmap.section.port.in;

import com.process.clash.application.roadmap.section.data.CreateSectionData;

public interface CreateSectionUseCase {
    CreateSectionData.Result execute(CreateSectionData.Command command);
}
