package com.process.clash.application.roadmap.section.port.in;

import com.process.clash.application.roadmap.section.data.UpdateSectionData;

public interface UpdateSectionUseCase {
    UpdateSectionData.Result execute(UpdateSectionData.Command command);
}
