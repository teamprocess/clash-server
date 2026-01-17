package com.process.clash.application.roadmap.section.common;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.port.out.UserSectionProgressRepositoryPort;
import com.process.clash.application.roadmap.section.exception.exception.forbidden.SectionAccessDeniedException;
import com.process.clash.domain.roadmap.entity.Section;
import com.process.clash.domain.roadmap.entity.UserSectionProgress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SectionCompleteChecker {

    private final UserSectionProgressRepositoryPort userSectionProgressRepository;

    protected void check(Actor actor, Section section) {
        Set<Section> prerequisites = section.getPrerequisites();
        if (prerequisites.isEmpty()) {
            return;
        }

        List<Long> prerequisiteIds = prerequisites.stream()
                .map(Section::getId)
                .toList();

        List<UserSectionProgress> userSectionProgresses =
                userSectionProgressRepository.findAllByUserIdAndSectionIdIn(actor.id(), prerequisiteIds);

        // 선행 로드맵을 시작하지 않은 경우
        if (prerequisites.size() != userSectionProgresses.size()) {
            throw new SectionAccessDeniedException();
        }

        // 선행 로드맵을 모두 완료하지 않은 경우
        boolean allCompleted = userSectionProgresses.stream()
                .allMatch(UserSectionProgress::getIsCompleted);
        if (!allCompleted) {
            throw new SectionAccessDeniedException();
        }
    }
}
