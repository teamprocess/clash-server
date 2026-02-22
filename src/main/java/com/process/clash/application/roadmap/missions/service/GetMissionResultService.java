package com.process.clash.application.roadmap.missions.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.missions.data.GetMissionResultData;
import com.process.clash.application.roadmap.missions.exception.exception.notfound.ChapterNotFoundException;
import com.process.clash.application.roadmap.missions.exception.exception.notfound.MissionNotFoundException;
import com.process.clash.application.roadmap.missions.port.in.GetMissionResultUseCase;
import com.process.clash.application.roadmap.port.out.ChapterRepositoryPort;
import com.process.clash.application.roadmap.port.out.MissionRepositoryPort;
import com.process.clash.application.roadmap.port.out.UserMissionHistoryRepositoryPort;
import com.process.clash.domain.roadmap.entity.Chapter;
import com.process.clash.domain.roadmap.entity.Mission;
import com.process.clash.domain.roadmap.entity.UserMissionHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetMissionResultService implements GetMissionResultUseCase {

    private final UserMissionHistoryRepositoryPort userMissionHistoryRepositoryPort;
    private final MissionRepositoryPort missionRepositoryPort;
    private final ChapterRepositoryPort chapterRepositoryPort;

    @Override
    public GetMissionResultData.Result execute(GetMissionResultData.Command command) {
        Actor actor = command.actor();
        Long missionId = command.missionId();

        Mission mission = missionRepositoryPort.findByIdWithQuestions(missionId)
                .orElseThrow(MissionNotFoundException::new);

        Chapter chapter = chapterRepositoryPort.findById(mission.getChapterId())
                .orElseThrow(ChapterNotFoundException::new);

        Optional<UserMissionHistory> historyOpt = userMissionHistoryRepositoryPort.findByUserIdAndMissionId(actor.id(), missionId);

        UserMissionHistory history = historyOpt.orElseGet(() -> UserMissionHistory.create(
                actor.id(),
                missionId,
                Optional.ofNullable(mission.getQuestions()).map(List::size).orElse(0)
        ));

        Long nextMissionId = null;
        Integer nextMissionOrderIndex = null;
        Long nextChapterId = null;
        Integer nextChapterOrderIndex = null;

        if (history.isCleared()) {
            List<Mission> missions = missionRepositoryPort.findAllByChapterId(mission.getChapterId());
            List<Long> missionIds = missions.stream().map(Mission::getId).toList();

            List<UserMissionHistory> allHistories = userMissionHistoryRepositoryPort.findAllByUserIdAndMissionIdIn(actor.id(), missionIds);
            Map<Long, UserMissionHistory> historyMap = allHistories.stream()
                    .collect(Collectors.toMap(UserMissionHistory::getMissionId, Function.identity()));

            // 미클리어된 미션 중 orderIndex가 가장 낮은 것 (현재 미션은 이미 클리어됨)
            Optional<Mission> nextMission = missions.stream()
                    .filter(m -> m.getOrderIndex() != null)
                    .sorted((m1, m2) -> Integer.compare(m1.getOrderIndex(), m2.getOrderIndex()))
                    .filter(m -> {
                        if (m.getId().equals(missionId)) {
                            return false;
                        }
                        UserMissionHistory h = historyMap.get(m.getId());
                        return h == null || !h.isCleared();
                    })
                    .findFirst();

            if (nextMission.isPresent()) {
                nextMissionId = nextMission.get().getId();
                nextMissionOrderIndex = nextMission.get().getOrderIndex();
            } else {
                List<Chapter> chapters = chapterRepositoryPort.findAllBySectionId(chapter.getSectionId());
                Optional<Chapter> nextChapter = chapters.stream()
                        .filter(c -> c.getOrderIndex() > chapter.getOrderIndex())
                        .min((a, b) -> a.getOrderIndex().compareTo(b.getOrderIndex()));

                if (nextChapter.isPresent()) {
                    nextChapterId = nextChapter.get().getId();
                    nextChapterOrderIndex = nextChapter.get().getOrderIndex();
                }
            }
        }

        return new GetMissionResultData.Result(
                missionId,
                history.isCleared(),
                history.getCorrectCount(),
                history.getTotalCount(),
                nextMissionId,
                nextMissionOrderIndex,
                nextChapterId,
                nextChapterOrderIndex
        );
    }
}