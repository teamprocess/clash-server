package com.process.clash.application.roadmap.v2.question.service;

import com.process.clash.application.roadmap.v2.port.out.QuestionV2RepositoryPort;
import com.process.clash.application.roadmap.v2.question.data.UpdateQuestionV2Data;
import com.process.clash.application.roadmap.v2.question.exception.exception.notfound.QuestionV2NotFoundException;
import com.process.clash.application.roadmap.v2.question.port.in.UpdateQuestionV2UseCase;
import com.process.clash.domain.roadmap.v2.entity.QuestionV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateQuestionV2Service implements UpdateQuestionV2UseCase {

    private final QuestionV2RepositoryPort questionV2RepositoryPort;

    @Override
    @Transactional
    public UpdateQuestionV2Data.Result execute(UpdateQuestionV2Data.Command command) {
        QuestionV2 question = questionV2RepositoryPort.findById(command.questionId())
                .orElseThrow(QuestionV2NotFoundException::new);

        QuestionV2 updatedQuestion = new QuestionV2(
                question.getId(),
                question.getChapterId(),
                command.content(),
                command.explanation(),
                command.orderIndex(),
                command.difficulty(),
                question.getChoices()
        );

        QuestionV2 savedQuestion = questionV2RepositoryPort.save(updatedQuestion);
        return UpdateQuestionV2Data.Result.from(savedQuestion);
    }
}
