package com.process.clash.application.roadmap.section.service;

import com.process.clash.application.roadmap.section.data.UpdateSectionData;
import com.process.clash.application.roadmap.section.exception.exception.notfound.SectionNotFoundException;
import com.process.clash.application.roadmap.section.port.in.UpdateSectionUseCase;
import com.process.clash.application.roadmap.section.port.out.SectionRepositoryPort;
import com.process.clash.domain.common.policy.CheckAdminPolicy;
import com.process.clash.domain.roadmap.entity.Section;
import com.process.clash.domain.roadmap.entity.SectionKeyPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateSectionService implements UpdateSectionUseCase {

    private final SectionRepositoryPort sectionRepository;
    private final CheckAdminPolicy checkAdminPolicy;

    @Override
    public UpdateSectionData.Result execute(UpdateSectionData.Command command) {
        checkAdminPolicy.check(command.actor());

        // 기존 Section 조회
        Section section = sectionRepository.findById(command.sectionId())
                .orElseThrow(SectionNotFoundException::new);

        // 업데이트된 Section 업데이트/저장
        Section updatedSection = sectionRepository.save(section.update(command));

        // 저장된 Section에서 keyPoints 추출
        List<String> keyPointContents = updatedSection.getKeyPoints() != null
                ? updatedSection.getKeyPoints().stream()
                        .map(SectionKeyPoint::getContent)
                        .toList()
                : List.of();

        return UpdateSectionData.Result.from(updatedSection, keyPointContents);
    }
}
