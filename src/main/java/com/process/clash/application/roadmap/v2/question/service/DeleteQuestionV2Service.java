package com.process.clash.application.roadmap.v2.question.service;

import com.process.clash.application.roadmap.v2.port.out.QuestionV2RepositoryPort;
import com.process.clash.application.roadmap.v2.question.data.DeleteQuestionV2Data;
import com.process.clash.application.roadmap.v2.question.port.in.DeleteQuestionV2UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteQuestionV2Service implements DeleteQuestionV2UseCase {

    private final QuestionV2RepositoryPort questionV2RepositoryPort;

    @Override
    @Transactional
    public void execute(DeleteQuestionV2Data.Command command) {
        questionV2RepositoryPort.deleteById(command.questionId());
    }
}
