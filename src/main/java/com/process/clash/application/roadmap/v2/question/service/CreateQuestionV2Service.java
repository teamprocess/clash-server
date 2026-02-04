package com.process.clash.application.roadmap.v2.question.service;

import com.process.clash.application.roadmap.v2.port.out.QuestionV2RepositoryPort;
import com.process.clash.application.roadmap.v2.question.data.CreateQuestionV2Data;
import com.process.clash.application.roadmap.v2.question.port.in.CreateQuestionV2UseCase;
import com.process.clash.domain.roadmap.v2.entity.QuestionV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateQuestionV2Service implements CreateQuestionV2UseCase {

    private final QuestionV2RepositoryPort questionV2RepositoryPort;

    @Override
    @Transactional
    public CreateQuestionV2Data.Result execute(CreateQuestionV2Data.Command command) {
        QuestionV2 question = command.toDomain();
        QuestionV2 savedQuestion = questionV2RepositoryPort.save(question);
        return CreateQuestionV2Data.Result.from(savedQuestion);
    }
}
