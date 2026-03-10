package com.process.clash.application.roadmap.section.service;

import com.process.clash.application.roadmap.section.data.DeleteSectionData;
import com.process.clash.application.roadmap.section.port.in.DeleteSectionUseCase;
import com.process.clash.application.roadmap.section.port.out.SectionRepositoryPort;
import com.process.clash.application.common.policy.CheckAdminPolicy;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
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
