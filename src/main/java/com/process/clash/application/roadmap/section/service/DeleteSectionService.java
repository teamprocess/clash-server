package com.process.clash.application.roadmap.section.service;

import com.process.clash.application.roadmap.section.port.out.SectionRepositoryPort;
import com.process.clash.application.roadmap.section.data.DeleteSectionData;
import com.process.clash.application.roadmap.section.port.in.DeleteSectionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteSectionService implements DeleteSectionUseCase {

    private final SectionRepositoryPort sectionRepository;

    @Override
    public void execute(DeleteSectionData.Command command) {

    }
}
