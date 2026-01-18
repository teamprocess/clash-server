package com.process.clash.application.roadmap.section.service;

import com.process.clash.application.roadmap.section.data.CreateSectionData;
import com.process.clash.application.roadmap.section.port.in.CreateSectionUseCase;
import com.process.clash.application.roadmap.section.port.out.SectionRepositoryPort;
import com.process.clash.domain.common.policy.CheckAdminPolicy;
import com.process.clash.domain.roadmap.entity.Section;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateSectionService implements CreateSectionUseCase {

    private final SectionRepositoryPort sectionRepositoryPort;
    private final CheckAdminPolicy checkAdminPolicy;

    @Override
    @Transactional
    public CreateSectionData.Result execute(CreateSectionData.Command command) {
        checkAdminPolicy.check(command.actor());

        // 같은 Major의 Section 개수를 조회하여 마지막 순서로 orderIndex 할당
        List<Section> existingSections = sectionRepositoryPort.findAllByMajor(command.major());
        int nextOrderIndex = existingSections.size();  // 0부터 시작 (0, 1, 2, ...)

        Section section = command.toDomain(nextOrderIndex);

        Section savedSection = sectionRepositoryPort.save(section);

        return CreateSectionData.Result.from(savedSection, command.keyPoints());
    }
}
