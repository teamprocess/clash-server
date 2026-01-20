package com.process.clash.application.roadmap.missions.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.missions.data.SubmitMissionAnswerData;
import com.process.clash.application.roadmap.missions.exception.exception.badrequest.ChapterLockedException;
import com.process.clash.application.roadmap.missions.exception.exception.badrequest.InvalidChoiceException;
import com.process.clash.application.roadmap.missions.exception.exception.notfound.ChapterNotFoundException;
import com.process.clash.application.roadmap.missions.exception.exception.notfound.MissionNotFoundException;
import com.process.clash.application.roadmap.missions.exception.exception.notfound.QuestionNotFoundException;
import com.process.clash.application.roadmap.missions.port.in.SubmitMissionAnswerUseCase;
import com.process.clash.application.roadmap.port.out.ChapterRepositoryPort;
import com.process.clash.application.roadmap.port.out.MissionRepositoryPort;
import com.process.clash.application.roadmap.port.out.UserMissionHistoryRepositoryPort;
import com.process.clash.application.roadmap.port.out.UserSectionProgressRepositoryPort;
import com.process.clash.domain.roadmap.entity.Chapter;
import com.process.clash.domain.roadmap.entity.Choice;
import com.process.clash.domain.roadmap.entity.Mission;
import com.process.clash.domain.roadmap.entity.MissionQuestion;
import com.process.clash.domain.roadmap.entity.UserMissionHistory;
import com.process.clash.domain.roadmap.entity.UserSectionProgress;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubmitMissionAnswerService implements SubmitMissionAnswerUseCase {

    private final MissionRepositoryPort missionRepositoryPort;
    private final UserMissionHistoryRepositoryPort userMissionHistoryRepositoryPort;
    private final ChapterRepositoryPort chapterRepositoryPort;
    private final UserSectionProgressRepositoryPort userSectionProgressRepositoryPort;

    @Override
    @Transactional
    public SubmitMissionAnswerData.Result execute(SubmitMissionAnswerData.Command command) {
        Actor actor = command.actor();
        // 미션 조회 (N+1 방지: questions와 choices 함께 fetch)
        Mission mission = missionRepositoryPort.findByIdWithQuestions(command.missionId())
                .orElseThrow(MissionNotFoundException::new);

        // 챕터 접근 권한 확인
        Chapter chapter = chapterRepositoryPort.findById(mission.getChapterId())
                .orElseThrow(ChapterNotFoundException::new);
        UserSectionProgress progress = userSectionProgressRepositoryPort.findByUserIdAndSectionId(actor.id(), chapter.getSectionId())
                .orElse(null);
        if (progress != null && progress.getCurrentChapterId() != null) {
            Chapter currentChapter = chapterRepositoryPort.findById(progress.getCurrentChapterId())
                    .orElseThrow(ChapterNotFoundException::new);
            if (currentChapter.getOrderIndex() < chapter.getOrderIndex()) {
                throw new ChapterLockedException();
            }
        } else {
            throw new ChapterLockedException();
        }

        // 질문 조회
        MissionQuestion question = Optional.ofNullable(mission.getQuestions())
                .orElse(List.of())
                .stream()
                .filter(q -> q.getId().equals(command.questionId()))
                .findFirst()
                .orElseThrow(QuestionNotFoundException::new);

        // 선택지 검증
        List<Choice> choices = Optional.ofNullable(question.getChoices()).orElse(List.of());

        Choice submittedChoice = choices.stream()
                .filter(c -> c.getId().equals(command.submittedChoiceId()))
                .findFirst()
                .orElseThrow(InvalidChoiceException::new);

        boolean isCorrect = submittedChoice.isCorrect();

        Long correctChoiceId = choices.stream()
                .filter(Choice::isCorrect)
                .findFirst()
                .map(Choice::getId)
                .orElse(null);

        // 사용자 미션 히스토리 조회 또는 생성
        Optional<UserMissionHistory> historyOpt = userMissionHistoryRepositoryPort.findByUserIdAndMissionId(actor.id(), command.missionId());
        UserMissionHistory history;
        if (historyOpt.isPresent()) {
            history = historyOpt.get();
        } else {
            history = UserMissionHistory.create(
                    actor.id(),
                    command.missionId(),
                    Optional.ofNullable(mission.getQuestions()).map(List::size).orElse(0)
            );
        }

        // 정답이면 correctCount 증가
        if (isCorrect) {
            history.recordCorrectAnswer();
        }

        // 답을 제출했으므로 currentQuestionIndex 증가
        history.recordQuestionAttempt();

        // 히스토리 저장
        userMissionHistoryRepositoryPort.save(history);

        // 챕터 내 모든 미션 조회
        List<Mission> missionsInChapter = missionRepositoryPort.findAllByChapterId(chapter.getId());
        List<Long> missionIdsInChapter = missionsInChapter.stream().map(Mission::getId).toList();

        // 현재 챕터의 미션 기록만 조회
        List<UserMissionHistory> chapterHistories = userMissionHistoryRepositoryPort.findAllByUserIdAndMissionIdIn(actor.id(), missionIdsInChapter);

        // histories를 missionId로 매핑 (O(1) 조회를 위해)
        Map<Long, UserMissionHistory> historyMap = chapterHistories.stream()
                .collect(Collectors.toMap(UserMissionHistory::getMissionId, Function.identity()));

        // mission을 missionId로 매핑
        Map<Long, Mission> missionMap = missionsInChapter.stream()
                .collect(Collectors.toMap(Mission::getId, Function.identity()));

        // 정렬된 미션 목록
        List<Mission> sortedMissions = missionsInChapter.stream()
                .filter(m -> m.getOrderIndex() != null)
                .sorted((m1, m2) -> Integer.compare(m1.getOrderIndex(), m2.getOrderIndex()))
                .toList();

        // 챕터 클리어 여부 확인
        boolean isChapterCleared = sortedMissions.stream()
                .allMatch(m -> {
                    UserMissionHistory h = historyMap.get(m.getId());
                    return h != null && h.isCleared();
                });

        // 챕터 완료 여부 확인 및 섹션 업데이트
        if (history.getCurrentQuestionIndex() >= history.getTotalCount()) {
            updateSectionProgress(chapter, progress);
        }

        // 진행 상황 계산
        int currentProgress = history.getCurrentQuestionIndex();
        int totalQuestion = history.getTotalCount();

        // 미션 클리어 여부 확인
        boolean isMissionCleared = history.isCleared();

        // 다음 미션 계산
        Long nextMissionId = null;
        Integer nextMissionOrderIndex = null;
        if (isMissionCleared) {
            for (Mission m : sortedMissions) {
                UserMissionHistory h = historyMap.get(m.getId());
                if (h == null || !h.isCleared()) {
                    nextMissionId = m.getId();
                    nextMissionOrderIndex = m.getOrderIndex();
                    break;
                }
            }
        }

        // 다음 챕터 계산
        Long nextChapterId = null;
        Integer nextChapterOrderIndex = null;
        if (isChapterCleared) {
            List<Chapter> chaptersInSection = chapterRepositoryPort.findAllBySectionId(chapter.getSectionId());
            Optional<Chapter> nextChapterOpt = chaptersInSection.stream()
                    .filter(c -> c.getOrderIndex() > chapter.getOrderIndex())
                    .min((c1, c2) -> Integer.compare(c1.getOrderIndex(), c2.getOrderIndex()));

            if (nextChapterOpt.isPresent()) {
                nextChapterId = nextChapterOpt.get().getId();
                nextChapterOrderIndex = nextChapterOpt.get().getOrderIndex();
            }
        }

        return new SubmitMissionAnswerData.Result(
                isCorrect,
                question.getExplanation(),
                currentProgress,
                totalQuestion,
                correctChoiceId,
                isMissionCleared,
                nextMissionId,
                nextMissionOrderIndex,
                isChapterCleared,
                nextChapterId,
                nextChapterOrderIndex
        );
    }

    private void updateSectionProgress(Chapter chapter, UserSectionProgress progress) {
        // 현재 섹션 내에서 다음 순서(OrderIndex)의 챕터를 찾음
        List<Chapter> chaptersInSection = chapterRepositoryPort.findAllBySectionId(chapter.getSectionId());
        Optional<Chapter> nextChapterOpt = chaptersInSection.stream()
                .filter(c -> c.getOrderIndex() > chapter.getOrderIndex())
                .min((c1, c2) -> Integer.compare(c1.getOrderIndex(), c2.getOrderIndex()));

        if (nextChapterOpt.isPresent()) {
            // 다음 챕터가 있으면 currentChapterId를 갱신하고 완료 챕터 수 +1
            progress.moveToNextChapter(nextChapterOpt.get().getId());
        } else {
            // 더 이상 다음 챕터가 없으면 섹션 전체 완료 처리
            progress.completeSection();
        }
        userSectionProgressRepositoryPort.save(progress);
    }
}
