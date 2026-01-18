package com.process.clash.application.roadmap.section.service;

import com.process.clash.application.common.policy.CheckAdminPolicy;
import com.process.clash.application.roadmap.section.data.UpdateSectionData;
import com.process.clash.application.roadmap.section.exception.exception.notfound.SectionNotFoundException;
import com.process.clash.application.roadmap.section.exception.exception.unprocessableentity.SectionCircularDependencyException;
import com.process.clash.application.roadmap.section.port.in.UpdateSectionUseCase;
import com.process.clash.application.roadmap.section.port.out.SectionRepositoryPort;
import com.process.clash.domain.roadmap.entity.Section;
import com.process.clash.domain.roadmap.entity.SectionKeyPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UpdateSectionService implements UpdateSectionUseCase {

    private final SectionRepositoryPort sectionRepository;
    private final CheckAdminPolicy checkAdminPolicy;

    @Override
    @Transactional
    public UpdateSectionData.Result execute(UpdateSectionData.Command command) {
        checkAdminPolicy.check(command.actor());

        // 기존 Section 조회
        Section section = sectionRepository.findById(command.sectionId())
                .orElseThrow(SectionNotFoundException::new);

        // orderIndex 변경이 요청된 경우, 다른 Section들 재정렬 (Insert & Shift)
        if (command.orderIndex() != null && !command.orderIndex().equals(section.getOrderIndex())) {
            reorderSections(section, command.orderIndex());
        }

        // prerequisiteSectionIds가 제공된 경우, 선행 로드맵 설정
        if (command.prerequisiteSectionIds() != null) {
            updatePrerequisites(section, command.prerequisiteSectionIds());
        }

        if (command.keyPoints() != null) {
            section.updateKeyPoints(command.keyPoints());
        }

        section.update(
                command.title(),
                command.category(),
                command.description(),
                command.orderIndex()
        );

        // 업데이트된 Section 저장
        Section updatedSection = sectionRepository.save(section);

        // 저장된 Section에서 keyPoints 추출
        List<String> keyPointContents = command.keyPoints() != null
                ? command.keyPoints()
                : (updatedSection.getKeyPoints() != null
                        ? updatedSection.getKeyPoints().stream()
                                .map(SectionKeyPoint::getContent)
                                .toList()
                        : List.of());

        return UpdateSectionData.Result.from(updatedSection, keyPointContents);
    }

    private void reorderSections(Section targetSection, int newOrderIndex) {
        // 같은 Major의 모든 Section 조회
        List<Section> sections = sectionRepository.findAllByMajor(targetSection.getMajor());
        int oldOrderIndex = targetSection.getOrderIndex();

        for (Section section : sections) {
            // 대상 Section은 건너뜀 (나중에 update()에서 처리)
            if (section.getId().equals(targetSection.getId())) {
                continue;
            }

            int currentIndex = section.getOrderIndex();

            // 앞으로 이동하는 경우: 기존 위치의 Section들을 뒤로 밀기
            // 예: C(2)를 1로 이동 → B(1)는 2로, C(2)는 1로
            if (newOrderIndex <= currentIndex && currentIndex < oldOrderIndex) {
                section.updateOrderIndex(currentIndex + 1);
            }
            // 뒤로 이동하는 경우: 새 위치까지의 Section들을 앞으로 당기기
            // 예: A(0)를 2로 이동 → B(1)는 0으로, C(2)는 1로, A(0)는 2로
            else if (oldOrderIndex < currentIndex && currentIndex <= newOrderIndex) {
                section.updateOrderIndex(currentIndex - 1);
            }
        }
    }

    private void updatePrerequisites(Section section, List<Long> prerequisiteSectionIds) {

        if (prerequisiteSectionIds.contains(section.getId())) {
            throw new SectionCircularDependencyException();
        }

        Set<Long> distinctIds = new HashSet<>(prerequisiteSectionIds);

        // 기존 prerequisites 초기화
        section.getPrerequisites().clear();

        if (distinctIds.isEmpty()) {
            return;
        }

        // 한 번에 모든 prerequisite Section 조회
        List<Section> prerequisiteSections = sectionRepository.findAllById(prerequisiteSectionIds);

        // 요청한 ID가 모두 존재하는지 확인
        if (prerequisiteSections.size() != distinctIds.size()) {
            throw new SectionNotFoundException();
        }

        prerequisiteSections.forEach(section::addPrerequisite);
    }
}
