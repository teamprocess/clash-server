package com.process.clash.application.roadmap.section.service;

import com.process.clash.application.roadmap.port.out.UserSectionProgressRepositoryPort;
import com.process.clash.application.roadmap.section.common.SectionCompleteChecker;
import com.process.clash.application.roadmap.section.data.GetSectionDetailsData;
import com.process.clash.application.roadmap.section.exception.exception.notfound.SectionNotFoundException;
import com.process.clash.application.roadmap.section.port.in.GetSectionDetailsUseCase;
import com.process.clash.application.roadmap.section.port.out.SectionRepositoryPort;
import com.process.clash.domain.roadmap.entity.Chapter;
import com.process.clash.domain.roadmap.entity.Section;
import com.process.clash.domain.roadmap.entity.UserSectionProgress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetSectionDetailsService implements GetSectionDetailsUseCase {

    private final SectionRepositoryPort sectionRepositoryPort;
    private final UserSectionProgressRepositoryPort userSectionProgressRepository;
    private final SectionCompleteChecker sectionCompleteChecker;

    @Override
    public GetSectionDetailsData.Result execute(GetSectionDetailsData.Command command) {
        Section section = sectionRepositoryPort.findById(command.sectionId())
                .orElseThrow(SectionNotFoundException::new);

        // 선행 로드맵 완료 여부 확인 (미완료 시 SectionAccessDeniedException 발생)
        sectionCompleteChecker.check(command.actor(), section);

        UserSectionProgress userSectionProgress = userSectionProgressRepository
                .findByUserIdAndSectionId(command.actor().id(), command.sectionId())
                .orElse(new UserSectionProgress(null, command.actor().id(), command.sectionId(), null, 0, false));

        Long currentChapterId = userSectionProgress.getCurrentChapterId();

        Integer currentOrderIndex = null;
        if (currentChapterId != null && section.getChapters() != null) {
            currentOrderIndex = section.getChapters().stream()
                    .filter(chapter -> chapter.getId().equals(currentChapterId))
                    .findFirst()
                    .map(Chapter::getOrderIndex)
                    .orElse(null);
        }

        return GetSectionDetailsData.Result.from(section, currentChapterId, currentOrderIndex);
    }
}
