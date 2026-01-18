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

    public void check(Actor actor, Section section) {
        Set<Section> prerequisites = section.getPrerequisites();
        if (prerequisites.isEmpty()) {
            return;
        }

        List<Long> prerequisiteIds = prerequisites.stream()
                .map(Section::getId)
                .toList();

        List<UserSectionProgress> userSectionProgresses =
                userSectionProgressRepository.findAllByUserIdAndSectionIdIn(actor.id(), prerequisiteIds);

        // 완료한 개수와 선행 로드맵 개수를 비교함
        long completedCount = userSectionProgresses.stream()
                .filter(UserSectionProgress::getIsCompleted)
                .count();

        if (completedCount != prerequisites.size()) {
            throw new SectionAccessDeniedException();
        }
    }
}
