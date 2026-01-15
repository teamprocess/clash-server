package com.process.clash.application.roadmap.section.service;

import com.process.clash.application.roadmap.section.port.out.SectionRepositoryPort;
import com.process.clash.application.roadmap.section.data.CreateSectionData;
import com.process.clash.application.roadmap.section.port.in.CreateSectionUseCase;
import com.process.clash.domain.common.policy.CheckAdminPolicy;
import com.process.clash.domain.roadmap.entity.Section;
import com.process.clash.domain.roadmap.entity.SectionKeyPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateSectionService implements CreateSectionUseCase {

    private final SectionRepositoryPort sectionRepositoryPort;
    private final CheckAdminPolicy checkAdminPolicy;

    @Override
    public CreateSectionData.Result execute(CreateSectionData.Command command) {
        checkAdminPolicy.check(command.actor());

        Section section = command.toDomain();

        Section savedSection = sectionRepositoryPort.save(section);

        // 저장된 Section에서 keyPoints 추출
        List<String> keyPointContents = savedSection.getKeyPoints() != null
                ? savedSection.getKeyPoints().stream()
                        .map(SectionKeyPoint::getContent)
                        .toList()
                : List.of();

        return CreateSectionData.Result.from(savedSection, keyPointContents);
    }
}
