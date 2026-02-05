package com.process.clash.application.roadmap.v2.question.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.v2.port.out.UserQuestionHistoryV2RepositoryPort;
import com.process.clash.application.roadmap.v2.question.data.ResetChapterV2Data;
import com.process.clash.application.roadmap.v2.question.port.in.ResetChapterV2UseCase;
import com.process.clash.domain.roadmap.v2.entity.UserQuestionHistoryV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Deprecated
@RequiredArgsConstructor
// 챕터 통과 직후에 다시 첫 문제를 풀어 리셋하지 않으면 리셋 불가 처리하도록 로직이 변경되어 만료된 서비스
public class ResetChapterV2Service implements ResetChapterV2UseCase {

    private final UserQuestionHistoryV2RepositoryPort userQuestionHistoryV2RepositoryPort;

    @Override
    @Transactional
    public void execute(ResetChapterV2Data.Command command) {
        Actor actor = command.actor();

        Optional<UserQuestionHistoryV2> historyOpt = userQuestionHistoryV2RepositoryPort
                .findByUserIdAndChapterId(actor.id(), command.chapterId());

        if (historyOpt.isPresent()) {
            UserQuestionHistoryV2 history = historyOpt.get();
            history.reset();
            userQuestionHistoryV2RepositoryPort.save(history);
        }
    }
}
