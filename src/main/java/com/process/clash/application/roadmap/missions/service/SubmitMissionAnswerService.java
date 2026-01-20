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

        // 1. 미션 조회 (N+1 방지: questions와 choices 함께 fetch)
        Mission mission = missionRepositoryPort.findByIdWithQuestions(command.missionId())
                .orElseThrow(MissionNotFoundException::new);

        // 2. 챕터 접근 권한 확인
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

        // 3. 질문 조회
        MissionQuestion question = Optional.ofNullable(mission.getQuestions())
                .orElse(List.of())
                .stream()
                .filter(q -> q.getId().equals(command.questionId()))
                .findFirst()
                .orElseThrow(QuestionNotFoundException::new);

        // 4. 선택지 검증 및 정답 확인
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

        // 5. 사용자 미션 히스토리 업데이트
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

        if (isCorrect) {
            history.recordCorrectAnswer();
        }
        history.recordQuestionAttempt(); // 정답 여부와 무관하게 시도 횟수/인덱스 증가

        userMissionHistoryRepositoryPort.save(history);

        // 6. 챕터 상태 계산을 위한 데이터 준비
        List<Mission> missionsInChapter = missionRepositoryPort.findAllByChapterId(chapter.getId());
        List<Long> missionIdsInChapter = missionsInChapter.stream().map(Mission::getId).toList();

        List<UserMissionHistory> chapterHistories = userMissionHistoryRepositoryPort.findAllByUserIdAndMissionIdIn(actor.id(), missionIdsInChapter);

        Map<Long, UserMissionHistory> historyMap = chapterHistories.stream()
                .collect(Collectors.toMap(UserMissionHistory::getMissionId, Function.identity()));

        List<Mission> sortedMissions = missionsInChapter.stream()
                .filter(m -> m.getOrderIndex() != null)
                .sorted((m1, m2) -> Integer.compare(m1.getOrderIndex(), m2.getOrderIndex()))
                .toList();

        // [수정] 챕터 클리어 여부 확인 (메모리 상의 최신 history 반영)
        boolean isChapterCleared = sortedMissions.stream()
                .allMatch(m -> {
                    // 현재 풀고 있는 미션인 경우, 방금 업데이트된 history 객체 사용
                    if (m.getId().equals(command.missionId())) {
                        return history.isCleared();
                    }
                    // 다른 미션은 DB에서 가져온 map 사용
                    UserMissionHistory h = historyMap.get(m.getId());
                    return h != null && h.isCleared();
                });

        // [수정] 섹션 진행도 업데이트 (조건 강화: 챕터 클리어 AND 현재 진행중인 챕터일 경우에만)
        // 기존: 미션 하나만 깨도 다음 챕터 열림 -> 수정: 챕터 전체 클리어 시 열림
        // 기존: 과거 챕터 다시 풀면 진행도 롤백 -> 수정: 현재 진행 중인 챕터일 때만 전진
        if (isChapterCleared && progress.getCurrentChapterId().equals(chapter.getId())) {
            updateSectionProgress(chapter, progress);
        }

        // 7. 응답 데이터 계산
        int currentProgress = history.getCurrentQuestionIndex();
        int totalQuestion = history.getTotalCount();
        boolean isMissionCleared = history.isCleared();

        // 다음 미션 계산 (아직 안 깬 미션 중 가장 빠른 순서)
        Long nextMissionId = null;
        Integer nextMissionOrderIndex = null;

        // 미션 클리어 시에만 다음 미션 찾기 시도
        if (isMissionCleared) {
            for (Mission m : sortedMissions) {
                // 현재 미션은 위에서 이미 클리어 확인됨.
                // 다른 미션들 중 안 깬거 찾기.
                // 주의: 현재 미션이 리스트에 있을 때 historyMap에는 구 데이터일 수 있으나,
                // 이미 isMissionCleared가 true이므로 루프에서 현재 미션 차례가 오면 패스하고 다음거 찾음.

                boolean isThisMissionCleared;
                if (m.getId().equals(command.missionId())) {
                    isThisMissionCleared = true;
                } else {
                    UserMissionHistory h = historyMap.get(m.getId());
                    isThisMissionCleared = (h != null && h.isCleared());
                }

                if (!isThisMissionCleared) {
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
        List<Chapter> chaptersInSection = chapterRepositoryPort.findAllBySectionId(chapter.getSectionId());
        Optional<Chapter> nextChapterOpt = chaptersInSection.stream()
                .filter(c -> c.getOrderIndex() > chapter.getOrderIndex())
                .min((c1, c2) -> Integer.compare(c1.getOrderIndex(), c2.getOrderIndex()));

        if (nextChapterOpt.isPresent()) {
            progress.moveToNextChapter(nextChapterOpt.get().getId());
        } else {
            progress.completeSection();
        }
        userSectionProgressRepositoryPort.save(progress);
    }
}