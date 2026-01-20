package com.process.clash.application.roadmap.chapter.service;

import com.process.clash.application.roadmap.chapter.data.GetChapterDetailsData;
import com.process.clash.application.roadmap.chapter.port.in.GetChapterDetailsUseCase;
import com.process.clash.application.roadmap.missions.exception.exception.notfound.ChapterNotFoundException;
import com.process.clash.application.roadmap.port.out.ChapterRepositoryPort;
import com.process.clash.application.roadmap.port.out.UserMissionHistoryRepositoryPort;
import com.process.clash.domain.roadmap.entity.Chapter;
import com.process.clash.domain.roadmap.entity.Mission;
import com.process.clash.domain.roadmap.entity.UserMissionHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetChapterDetailsService implements GetChapterDetailsUseCase {

    private final ChapterRepositoryPort chapterRepositoryPort;
    private final UserMissionHistoryRepositoryPort userMissionHistoryRepositoryPort;

    @Override
    public GetChapterDetailsData.Result execute(GetChapterDetailsData.Command command) {
        Chapter chapter = chapterRepositoryPort.findById(command.chapterId())
                .orElseThrow(ChapterNotFoundException::new);

        Long userId = command.actor().id();

        List<Mission> missions = chapter.getMissions() != null ? chapter.getMissions() : List.of();
        List<Long> missionIds = missions.stream()
                .map(Mission::getId)
                .toList();

        List<UserMissionHistory> histories = userMissionHistoryRepositoryPort.findAllByUserIdAndMissionIdIn(userId, missionIds);

        Long currentMissionId = null;
        Long currentQuestionId = null;
        Integer currentQuestionIndex = 0;
        Integer totalQuestions = 0;

        // missions를 orderIndex로 정렬
        List<Mission> sortedMissions = missions.stream()
                .filter(m -> m.getOrderIndex() != null)
                .sorted((m1, m2) -> Integer.compare(m1.getOrderIndex(), m2.getOrderIndex()))
                .toList();

        // histories를 missionId로 매핑
        Map<Long, UserMissionHistory> historyMap = histories.stream()
                .collect(Collectors.toMap(UserMissionHistory::getMissionId, h -> h));

        // 정렬된 missions에서 미완료 첫 번째 미션을 찾음
        for (Mission mission : sortedMissions) {
            UserMissionHistory history = historyMap.get(mission.getId());
            if (history == null || !history.isCleared()) {
                currentMissionId = mission.getId();
                currentQuestionIndex = history != null ? history.getCurrentQuestionIndex() : 0;
                totalQuestions = mission.getQuestions() != null ? mission.getQuestions().size() : 0;
                if (mission.getQuestions() != null && !mission.getQuestions().isEmpty()) {
                    var questionId = mission.getQuestions().stream()
                        .sorted(java.util.Comparator.comparing(com.process.clash.domain.roadmap.entity.MissionQuestion::getOrderIndex))
                        .skip(currentQuestionIndex)
                        .findFirst()
                        .map(q -> q.getId())
                        .orElse(null);
                    currentQuestionId = questionId;
                }
                break;
            }
        }

        return GetChapterDetailsData.Result.from(chapter, currentMissionId, currentQuestionId, currentQuestionIndex, totalQuestions);
    }
}
