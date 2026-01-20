package com.process.clash.application.roadmap.section.service;

import com.process.clash.application.roadmap.port.out.MissionRepositoryPort;
import com.process.clash.application.roadmap.port.out.UserMissionHistoryRepositoryPort;
import com.process.clash.application.roadmap.port.out.UserSectionProgressRepositoryPort;
import com.process.clash.application.roadmap.section.common.SectionCompleteChecker;
import com.process.clash.application.roadmap.section.data.GetSectionDetailsData;
import com.process.clash.application.roadmap.section.exception.exception.notfound.SectionNotFoundException;
import com.process.clash.application.roadmap.section.port.in.GetSectionDetailsUseCase;
import com.process.clash.application.roadmap.section.port.out.SectionRepositoryPort;
import com.process.clash.domain.roadmap.entity.Chapter;
import com.process.clash.domain.roadmap.entity.Mission;
import com.process.clash.domain.roadmap.entity.Section;
import com.process.clash.domain.roadmap.entity.UserMissionHistory;
import com.process.clash.domain.roadmap.entity.UserSectionProgress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetSectionDetailsService implements GetSectionDetailsUseCase {

    private final SectionRepositoryPort sectionRepositoryPort;
    private final UserSectionProgressRepositoryPort userSectionProgressRepository;
    private final MissionRepositoryPort missionRepositoryPort;
    private final UserMissionHistoryRepositoryPort userMissionHistoryRepository;
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

        List<Long> chapterIds = section.getChapters() != null
                ? section.getChapters().stream().map(Chapter::getId).toList()
                : List.of();

        List<Mission> missions = missionRepositoryPort.findAllByChapterIdIn(chapterIds);

        Map<Long, List<Mission>> chapterMissionsMap = missions.stream()
                .collect(Collectors.groupingBy(Mission::getChapterId));

        List<Long> missionIds = missions.stream().map(Mission::getId).toList();

        List<UserMissionHistory> histories = userMissionHistoryRepository.findAllByUserIdAndMissionIdIn(command.actor().id(), missionIds);

        Map<Long, UserMissionHistory> missionHistoryMap = histories.stream()
                .collect(Collectors.toMap(UserMissionHistory::getMissionId, h -> h));

        Integer currentMissionIndex = null;
        if (currentChapterId != null) {
            List<Mission> currentChapterMissions = chapterMissionsMap.getOrDefault(currentChapterId, List.of());
            List<Mission> sortedMissions = currentChapterMissions.stream()
                    .sorted((m1, m2) -> {
                        if (m1.getOrderIndex() == null) return 1;
                        if (m2.getOrderIndex() == null) return -1;
                        return Integer.compare(m1.getOrderIndex(), m2.getOrderIndex());
                    })
                    .toList();

            for (int i = 0; i < sortedMissions.size(); i++) {
                Mission mission = sortedMissions.get(i);
                UserMissionHistory history = missionHistoryMap.get(mission.getId());
                if (history == null || !history.isCleared()) {
                    currentMissionIndex = i;
                    break;
                }
            }
        }

        return GetSectionDetailsData.Result.from(section, currentChapterId, currentOrderIndex, currentMissionIndex, chapterMissionsMap, missionHistoryMap);
    }
}
