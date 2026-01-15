package com.process.clash.application.roadmap.section.service;

import com.process.clash.application.roadmap.section.port.out.SectionRepositoryPort;
import com.process.clash.application.roadmap.section.data.DeleteSectionData;
import com.process.clash.application.roadmap.section.port.in.DeleteSectionUseCase;
import com.process.clash.domain.common.policy.CheckAdminPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteSectionService implements DeleteSectionUseCase {

    private final SectionRepositoryPort sectionRepository;
    private final CheckAdminPolicy checkAdminPolicy;

    @Override
    public void execute(DeleteSectionData.Command command) {
        checkAdminPolicy.check(command.actor());

        sectionRepository.deleteById(command.sectionId());
    }
}
