package com.process.clash.application.roadmap.section.port.in;

import com.process.clash.application.roadmap.section.data.DeleteSectionData;

public interface DeleteSectionUseCase {
    void execute(DeleteSectionData.Command command);
}
