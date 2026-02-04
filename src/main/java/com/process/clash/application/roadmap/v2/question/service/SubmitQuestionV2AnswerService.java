package com.process.clash.application.roadmap.v2.question.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.port.out.UserSectionProgressRepositoryPort;
import com.process.clash.application.roadmap.v2.port.out.ChapterV2RepositoryPort;
import com.process.clash.application.roadmap.v2.port.out.QuestionV2RepositoryPort;
import com.process.clash.application.roadmap.v2.port.out.UserQuestionHistoryV2RepositoryPort;
import com.process.clash.application.roadmap.v2.question.data.SubmitQuestionV2AnswerData;
import com.process.clash.application.roadmap.v2.question.exception.exception.badrequest.ChapterV2LockedException;
import com.process.clash.application.roadmap.v2.question.exception.exception.badrequest.InvalidChoiceV2Exception;
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

        // 2. 챕터 조회 및 접근 권한 확인
        ChapterV2 chapter = chapterV2RepositoryPort.findById(question.getChapterId())
                .orElseThrow(ChapterV2NotFoundException::new);

        validateChapterAccess(actor, chapter);

        // 3. 선택지 검증 및 정답 확인
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

        // 4. 사용자 챕터 히스토리 업데이트
        Optional<UserQuestionHistoryV2> historyOpt = userQuestionHistoryV2RepositoryPort
                .findByUserIdAndChapterId(actor.id(), chapter.getId());

        UserQuestionHistoryV2 history;
        if (historyOpt.isPresent()) {
            history = historyOpt.get();
        } else {
            int totalQuestions = Optional.ofNullable(chapter.getQuestions()).map(List::size).orElse(0);
            history = UserQuestionHistoryV2.create(actor.id(), chapter.getId(), totalQuestions);
        }

        if (isCorrect) {
            history.recordCorrectAnswer();
        }
        history.recordQuestionAttempt();

        userQuestionHistoryV2RepositoryPort.save(history);

        // 5. 챕터 클리어 여부 확인
        boolean isChapterCleared = history.isCleared();

        // 6. 챕터 클리어 시 섹션 진행도 업데이트
        Long nextChapterId = null;
        Integer nextChapterOrderIndex = null;

        if (isChapterCleared) {
            UserSectionProgress progress = userSectionProgressRepositoryPort
                    .findByUserIdAndSectionId(actor.id(), chapter.getSectionId())
                    .orElse(null);

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

        // 7. 응답 반환
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

    private void validateChapterAccess(Actor actor, ChapterV2 chapter) {
        UserSectionProgress progress = userSectionProgressRepositoryPort
                .findByUserIdAndSectionId(actor.id(), chapter.getSectionId())
                .orElse(null);

        if (progress != null && progress.getCurrentChapterId() != null) {
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
