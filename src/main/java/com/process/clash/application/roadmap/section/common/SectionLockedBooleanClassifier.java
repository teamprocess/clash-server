package com.process.clash.application.roadmap.section.common;

import com.process.clash.domain.roadmap.entity.Section;
import com.process.clash.domain.roadmap.entity.UserSectionProgress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SectionLockedBooleanClassifier {

    public boolean checkWithProgressMap(Section section, Map<Long, UserSectionProgress> progressMap) {
        Set<Section> prerequisites = section.getPrerequisites();
        if (prerequisites.isEmpty()) {
            return false;
        }

        // 선행 로드맵을 시작하지 않았거나 완료하지 않은 경우 확인
        for (Section prerequisite : prerequisites) {
            UserSectionProgress progress = progressMap.get(prerequisite.getId());

            // 선행 로드맵을 시작하지 않은 경우
            if (progress == null) {
                return true;
            }

            // 선행 로드맵을 완료하지 않은 경우
            if (!progress.getIsCompleted()) {
                return true;
            }
        }

        return false;
    }
}
