package com.process.clash.application.roadmap.chapter.service;

import com.process.clash.application.roadmap.chapter.data.GetChapterDetailsData;
import com.process.clash.application.roadmap.chapter.port.in.GetChapterDetailsUseCase;
import com.process.clash.application.roadmap.missions.exception.exception.notfound.ChapterNotFoundException;
import com.process.clash.application.roadmap.port.out.ChapterRepositoryPort;
import com.process.clash.application.roadmap.port.out.MissionRepositoryPort;
import com.process.clash.application.roadmap.port.out.UserMissionHistoryRepositoryPort;
import com.process.clash.domain.roadmap.entity.Chapter;
import com.process.clash.domain.roadmap.entity.Mission;
import com.process.clash.domain.roadmap.entity.UserMissionHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetChapterDetailsService implements GetChapterDetailsUseCase {

    private final ChapterRepositoryPort chapterRepositoryPort;
    private final UserMissionHistoryRepositoryPort userMissionHistoryRepositoryPort;
    private final MissionRepositoryPort missionRepositoryPort;

    @Override
    public GetChapterDetailsData.Result execute(GetChapterDetailsData.Command command) {
        Chapter chapter = chapterRepositoryPort.findById(command.chapterId())
                .orElseThrow(ChapterNotFoundException::new);

        Long userId = command.actor().id();

        List<Mission> missions = missionRepositoryPort.findAllByChapterId(chapter.getId());
        List<Long> missionIds = missions.stream()
                .map(Mission::getId)
                .toList();

        List<UserMissionHistory> histories = userMissionHistoryRepositoryPort.findAllByUserIdAndMissionIdIn(userId, missionIds);

        Long currentMissionId = null;
        Long currentQuestionId = null;
        Integer currentQuestionIndex = 0;
        Integer totalQuestions = 0;

        if (!histories.isEmpty()) {
            UserMissionHistory currentHistory = histories.stream()
                    .filter(h -> !h.isCleared())
                    .findFirst()
                    .orElse(histories.get(0));

            currentMissionId = currentHistory.getMissionId();
            currentQuestionIndex = currentHistory.getCurrentQuestionIndex();
            totalQuestions = currentHistory.getTotalCount();

            Mission currentMission = missions.stream()
                    .filter(m -> m.getId().equals(currentHistory.getMissionId()))
                    .findFirst()
                    .orElse(missions.get(0));

            if (currentMission.getQuestions() != null && currentQuestionIndex < currentMission.getQuestions().size()) {
                currentQuestionId = currentMission.getQuestions().get(currentQuestionIndex).getId();
            }
        } else if (!missions.isEmpty()) {
            Mission firstMission = missions.get(0);
            currentMissionId = firstMission.getId();
            totalQuestions = firstMission.getQuestions() != null ? firstMission.getQuestions().size() : 0;
            if (firstMission.getQuestions() != null && !firstMission.getQuestions().isEmpty()) {
                currentQuestionId = firstMission.getQuestions().get(0).getId();
            }
        }

        return GetChapterDetailsData.Result.from(chapter, currentMissionId, currentQuestionId, currentQuestionIndex, totalQuestions);
    }
}
