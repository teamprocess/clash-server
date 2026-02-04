package com.process.clash.application.roadmap.v2.question.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.port.out.UserSectionProgressRepositoryPort;
import com.process.clash.application.roadmap.v2.port.out.ChapterV2RepositoryPort;
import com.process.clash.application.roadmap.v2.port.out.QuestionV2RepositoryPort;
import com.process.clash.application.roadmap.v2.port.out.UserQuestionHistoryV2RepositoryPort;
import com.process.clash.application.roadmap.v2.question.data.SubmitQuestionV2AnswerData;
import com.process.clash.application.roadmap.v2.question.exception.exception.badrequest.ChapterV2LockedException;
import com.process.clash.application.roadmap.v2.question.exception.exception.badrequest.InvalidChoiceV2Exception;
import com.process.clash.application.roadmap.v2.question.exception.exception.badrequest.InvalidQuestionOrderV2Exception;
import com.process.clash.application.roadmap.v2.question.exception.exception.notfound.ChapterV2NotFoundException;
import com.process.clash.application.roadmap.v2.question.exception.exception.notfound.QuestionV2NotFoundException;
import com.process.clash.application.roadmap.v2.question.port.in.SubmitQuestionV2AnswerUseCase;
import com.process.clash.domain.roadmap.entity.UserSectionProgress;
import com.process.clash.domain.roadmap.v2.entity.ChapterV2;
import com.process.clash.domain.roadmap.v2.entity.ChoiceV2;
import com.process.clash.domain.roadmap.v2.entity.QuestionV2;
import com.process.clash.domain.roadmap.v2.entity.UserQuestionHistoryV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubmitQuestionV2AnswerService implements SubmitQuestionV2AnswerUseCase {

    private final QuestionV2RepositoryPort questionV2RepositoryPort;
    private final ChapterV2RepositoryPort chapterV2RepositoryPort;
    private final UserQuestionHistoryV2RepositoryPort userQuestionHistoryV2RepositoryPort;
    private final UserSectionProgressRepositoryPort userSectionProgressRepositoryPort;

    @Override
    @Transactional
    public SubmitQuestionV2AnswerData.Result execute(SubmitQuestionV2AnswerData.Command command) {
        Actor actor = command.actor();

        // 1. 질문 조회
        QuestionV2 question = questionV2RepositoryPort.findById(command.questionId())
                .orElseThrow(QuestionV2NotFoundException::new);

        // 2. 챕터 조회 (questions와 choices 필요)
        ChapterV2 chapter = chapterV2RepositoryPort.findByIdWithQuestionsAndChoices(question.getChapterId())
                .orElseThrow(ChapterV2NotFoundException::new);

        // 3. 사용자 섹션 진행도 조회 (한 번만 조회하여 재사용)
        UserSectionProgress progress = userSectionProgressRepositoryPort
                .findByUserIdAndSectionId(actor.id(), chapter.getSectionId())
                .orElse(null);

        // 4. 접근 권한 확인
        validateChapterAccess(chapter, progress);

        // 5. 선택지 검증 및 정답 확인
        List<ChoiceV2> choices = Optional.ofNullable(question.getChoices()).orElse(List.of());

        ChoiceV2 submittedChoice = choices.stream()
                .filter(c -> c.getId().equals(command.submittedChoiceId()))
                .findFirst()
                .orElseThrow(InvalidChoiceV2Exception::new);

        boolean isCorrect = submittedChoice.isCorrect();

        Long correctChoiceId = choices.stream()
                .filter(ChoiceV2::isCorrect)
                .findFirst()
                .map(ChoiceV2::getId)
                .orElse(null);

        // 6. 사용자 챕터 히스토리 업데이트
        Optional<UserQuestionHistoryV2> historyOpt = userQuestionHistoryV2RepositoryPort
                .findByUserIdAndChapterId(actor.id(), chapter.getId());

        UserQuestionHistoryV2 history;
        if (historyOpt.isPresent()) {
            history = historyOpt.get();

            Integer questionOrderIndex = question.getOrderIndex();

            // 🔄 하이브리드 리셋 로직: 클리어한 챕터를 다시 시작하는 경우
            if (history.isCleared()) {
                if (questionOrderIndex != null && questionOrderIndex == 0) {
                    // 문제 1을 제출하면 자동으로 리셋하고 다시 시작
                    history.reset();
                } else {
                    // 문제 1이 아닌 다른 문제는 금지 (처음부터 다시 시작해야 함)
                    throw new InvalidQuestionOrderV2Exception();
                }
            } else {
                // 일반적인 순서 검증 (챕터가 클리어되지 않은 경우)
                // currentQuestionIndex는 "다음에 풀어야 할 문제의 orderIndex"를 의미
                if (questionOrderIndex != null && questionOrderIndex < history.getCurrentQuestionIndex()) {
                    // 이미 제출한 문제 (과거 문제)
                    throw new InvalidQuestionOrderV2Exception();
                }
                if (questionOrderIndex != null && questionOrderIndex > history.getCurrentQuestionIndex()) {
                    // 아직 제출할 수 없는 문제 (미래 문제)
                    throw new InvalidQuestionOrderV2Exception();
                }
            }
        } else {
            int totalQuestions = Optional.ofNullable(chapter.getQuestions()).map(List::size).orElse(0);
            history = UserQuestionHistoryV2.create(actor.id(), chapter.getId(), totalQuestions);

            // 처음 제출하는 경우, 첫 번째 문제(orderIndex=0)만 가능
            // orderIndex가 null이거나 0이 아닌 경우 모두 예외
            if (!Integer.valueOf(0).equals(question.getOrderIndex())) {
                throw new InvalidQuestionOrderV2Exception();
            }
        }

        if (isCorrect) {
            history.recordCorrectAnswer();
        }
        history.recordQuestionAttempt(question.getOrderIndex());

        userQuestionHistoryV2RepositoryPort.save(history);

        // 7. 챕터 클리어 여부 확인
        boolean isChapterCleared = history.isCleared();

        // 8. 챕터 클리어 시 섹션 진행도 업데이트 (조회된 progress 재사용)
        Long nextChapterId = null;
        Integer nextChapterOrderIndex = null;

        if (isChapterCleared) {
            // progress가 없고 첫 번째 챕터인 경우 새로 생성
            if (progress == null && chapter.getOrderIndex() == 0) {
                progress = UserSectionProgress.start(actor.id(), chapter.getSectionId(), chapter.getId());
            }

            if (progress != null && chapter.getId().equals(progress.getCurrentChapterId())) {
                // 다음 챕터 찾기
                List<ChapterV2> chaptersInSection = chapterV2RepositoryPort
                        .findAllBySectionId(chapter.getSectionId());

                Optional<ChapterV2> nextChapter = chaptersInSection.stream()
                        .filter(c -> c.getOrderIndex() != null)
                        .filter(c -> c.getOrderIndex() > chapter.getOrderIndex())
                        .min((c1, c2) -> Integer.compare(c1.getOrderIndex(), c2.getOrderIndex()));

                if (nextChapter.isPresent()) {
                    progress.moveToNextChapter(nextChapter.get().getId());
                    userSectionProgressRepositoryPort.save(progress);
                    nextChapterId = nextChapter.get().getId();
                    nextChapterOrderIndex = nextChapter.get().getOrderIndex();
                }
            }
        }

        // 9. 응답 반환
        return new SubmitQuestionV2AnswerData.Result(
                isCorrect,
                question.getExplanation(),
                history.getCurrentQuestionIndex(),
                history.getTotalCount(),
                correctChoiceId,
                isChapterCleared,
                nextChapterId,
                nextChapterOrderIndex
        );
    }

    /**
     * 챕터 접근 권한을 검증합니다.
     * 
     * @param chapter 접근하려는 챕터
     * @param progress 사용자의 섹션 진행도 (이미 조회된 객체)
     */
    private void validateChapterAccess(ChapterV2 chapter, UserSectionProgress progress) {
        if (progress != null && progress.getCurrentChapterId() != null) {
            // orderIndex만 필요하므로 JPA 기본 findById 사용 (Lazy Loading)
            ChapterV2 currentChapter = chapterV2RepositoryPort.findById(progress.getCurrentChapterId())
                    .orElseThrow(ChapterV2NotFoundException::new);

            Integer currentOrderIndex = currentChapter.getOrderIndex();
            Integer targetOrderIndex = chapter.getOrderIndex();

            if (currentOrderIndex == null || targetOrderIndex == null) {
                throw new ChapterV2LockedException();
            }

            if (currentOrderIndex < targetOrderIndex) {
                throw new ChapterV2LockedException();
            }
        } else {
            Integer targetOrderIndex = chapter.getOrderIndex();
            if (targetOrderIndex == null || targetOrderIndex != 0) {
                throw new ChapterV2LockedException();
            }
        }
    }
}
