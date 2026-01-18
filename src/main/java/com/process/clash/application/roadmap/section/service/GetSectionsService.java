package com.process.clash.application.roadmap.section.service;

import com.process.clash.application.roadmap.port.out.UserSectionProgressRepositoryPort;
import com.process.clash.application.roadmap.section.common.SectionLockedBooleanClassifier;
import com.process.clash.application.roadmap.section.data.GetSectionsData;
import com.process.clash.application.roadmap.section.port.in.GetSectionsUseCase;
import com.process.clash.application.roadmap.section.port.out.SectionRepositoryPort;
import com.process.clash.domain.roadmap.entity.Section;
import com.process.clash.domain.roadmap.entity.UserSectionProgress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetSectionsService implements GetSectionsUseCase {

    private final SectionRepositoryPort sectionRepository;
    private final SectionLockedBooleanClassifier sectionLockedBooleanClassifier;
    private final UserSectionProgressRepositoryPort userSectionProgressRepository;

    @Override
    public GetSectionsData.Result execute(GetSectionsData.Command command) {
        // Major별 Section 조회 (orderIndex로 자동 정렬됨)
        List<Section> sections = sectionRepository.findAllByMajor(command.major());

        // Section의 category를 중복 제거하여 추출
        List<String> categories = sections.stream()
                .map(Section::getCategory)
                .distinct()
                .toList();

        // 모든 섹션 ID 추출
        List<Long> sectionIds = sections.stream()
                .map(Section::getId)
                .toList();

        // 한 번에 모든 UserSectionProgress 조회 (N+1 문제 해결)
        List<UserSectionProgress> allProgresses =
                userSectionProgressRepository.findAllByUserIdAndSectionIdIn(command.actor().id(), sectionIds);

        // Map으로 변환
        Map<Long, UserSectionProgress> progressMap = allProgresses.stream()
                .collect(Collectors.toMap(UserSectionProgress::getSectionId, p -> p));

        // 각 Section에 대해 completed, locked 여부 판단
        List<GetSectionsData.Result.SectionVo> sectionVos = sections.stream()
                .map(section -> {
                    // locked 검사 (Map 활용, DB 조회 없음)
                    boolean locked = sectionLockedBooleanClassifier.checkWithProgressMap(section, progressMap);

                    // completed 검사 (Map 활용, DB 조회 없음)
                    UserSectionProgress progress = progressMap.get(section.getId());
                    boolean completed = progress != null && progress.getIsCompleted();

                    return GetSectionsData.Result.SectionVo.from(section, completed, locked);
                })
                .toList();

        return new GetSectionsData.Result(sectionVos, categories);
    }
}
