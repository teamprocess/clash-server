package com.process.clash.application.roadmap.v2.section.service;

import com.process.clash.application.roadmap.port.out.UserSectionProgressRepositoryPort;
import com.process.clash.application.roadmap.section.common.SectionLockedBooleanClassifier;
import com.process.clash.application.roadmap.section.port.out.SectionRepositoryPort;
import com.process.clash.application.roadmap.v2.section.data.GetSectionV2ListData;
import com.process.clash.application.roadmap.v2.section.port.in.GetSectionV2ListUseCase;
import com.process.clash.domain.roadmap.entity.Section;
import com.process.clash.domain.roadmap.entity.UserSectionProgress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetSectionV2ListService implements GetSectionV2ListUseCase {

    private final SectionRepositoryPort sectionRepository;
    private final SectionLockedBooleanClassifier sectionLockedBooleanClassifier;
    private final UserSectionProgressRepositoryPort userSectionProgressRepository;

    @Override
    public GetSectionV2ListData.Result execute(GetSectionV2ListData.Command command) {
        List<Section> sections = sectionRepository.findAllByMajor(command.major());

        List<Long> categories = sections.stream()
                .map(s -> s.getCategory().getId())
                .distinct()
                .toList();

        List<Long> sectionIds = sections.stream()
                .map(Section::getId)
                .toList();

        List<UserSectionProgress> allProgresses =
                userSectionProgressRepository.findAllByUserIdAndSectionIdIn(command.actor().id(), sectionIds);

        Map<Long, UserSectionProgress> progressMap = allProgresses.stream()
                .collect(Collectors.toMap(UserSectionProgress::getSectionId, p -> p));

        List<GetSectionV2ListData.Result.SectionVo> sectionVos = sections.stream()
                .map(section -> {
                    boolean locked = sectionLockedBooleanClassifier.checkWithProgressMap(section, progressMap);
                    UserSectionProgress progress = progressMap.get(section.getId());
                    boolean completed = progress != null && progress.getIsCompleted();
                    return GetSectionV2ListData.Result.SectionVo.from(section, completed, locked);
                })
                .toList();

        Integer completedSections = (int) sectionVos.stream()
                .filter(GetSectionV2ListData.Result.SectionVo::completed)
                .count();
        Integer totalSections = sections.size();

        return new GetSectionV2ListData.Result(sectionVos, categories, completedSections, totalSections);
    }
}
