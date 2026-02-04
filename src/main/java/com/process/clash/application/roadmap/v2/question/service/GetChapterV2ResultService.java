package com.process.clash.application.roadmap.v2.question.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.v2.port.out.UserQuestionHistoryV2RepositoryPort;
import com.process.clash.application.roadmap.v2.question.data.GetChapterV2ResultData;
import com.process.clash.application.roadmap.v2.question.port.in.GetChapterV2ResultUseCase;
import com.process.clash.domain.roadmap.v2.entity.UserQuestionHistoryV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetChapterV2ResultService implements GetChapterV2ResultUseCase {

    private final UserQuestionHistoryV2RepositoryPort userQuestionHistoryV2RepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public GetChapterV2ResultData.Result execute(GetChapterV2ResultData.Command command) {
        Actor actor = command.actor();

        UserQuestionHistoryV2 history = userQuestionHistoryV2RepositoryPort
                .findByUserIdAndChapterId(actor.id(), command.chapterId())
                .orElse(null);

        if (history == null) {
            return new GetChapterV2ResultData.Result(false, 0, 0, 0);
        }

        int scorePercentage = history.getTotalCount() > 0
                ? (history.getCorrectCount() * 100 / history.getTotalCount())
                : 0;

        return new GetChapterV2ResultData.Result(
                history.isCleared(),
                history.getCorrectCount(),
                history.getTotalCount(),
                scorePercentage
        );
    }
}
