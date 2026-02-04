package com.process.clash.application.roadmap.v2.chapter.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.v2.chapter.data.GetChapterV2DetailsData;
import com.process.clash.application.roadmap.v2.chapter.port.in.GetChapterV2DetailsUseCase;
import com.process.clash.application.roadmap.v2.port.out.ChapterV2RepositoryPort;
import com.process.clash.application.roadmap.v2.port.out.UserQuestionHistoryV2RepositoryPort;
import com.process.clash.application.roadmap.v2.question.exception.exception.notfound.ChapterV2NotFoundException;
import com.process.clash.domain.roadmap.v2.entity.ChapterV2;
import com.process.clash.domain.roadmap.v2.entity.UserQuestionHistoryV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetChapterV2DetailsService implements GetChapterV2DetailsUseCase {

    private final ChapterV2RepositoryPort chapterV2RepositoryPort;
    private final UserQuestionHistoryV2RepositoryPort userQuestionHistoryV2RepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public GetChapterV2DetailsData.Result execute(GetChapterV2DetailsData.Command command) {
        Actor actor = command.actor();

        // 챕터 조회 (questions와 choices 함께 fetch)
        ChapterV2 chapter = chapterV2RepositoryPort.findByIdWithQuestionsAndChoices(command.chapterId())
                .orElseThrow(ChapterV2NotFoundException::new);

        // 사용자 히스토리 조회
        Optional<UserQuestionHistoryV2> historyOpt = userQuestionHistoryV2RepositoryPort
                .findByUserIdAndChapterId(actor.id(), command.chapterId());

        Integer currentQuestionIndex = historyOpt.map(UserQuestionHistoryV2::getCurrentQuestionIndex).orElse(0);
        Integer correctCount = historyOpt.map(UserQuestionHistoryV2::getCorrectCount).orElse(0);
        boolean isCleared = historyOpt.map(UserQuestionHistoryV2::isCleared).orElse(false);

        return GetChapterV2DetailsData.Result.from(chapter, currentQuestionIndex, correctCount, isCleared);
    }
}
