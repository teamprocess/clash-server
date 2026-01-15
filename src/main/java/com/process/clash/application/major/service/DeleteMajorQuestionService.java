package com.process.clash.application.major.service;

import com.process.clash.application.major.data.DeleteMajorQuestionData;
import com.process.clash.application.major.exception.exception.notfound.MajorQuestionNotFoundException;
import com.process.clash.application.major.port.in.DeleteMajorQuestionUseCase;
import com.process.clash.application.major.port.out.MajorQuestionRepositoryPort;
import com.process.clash.domain.common.policy.CheckAdminPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteMajorQuestionService implements DeleteMajorQuestionUseCase {

    private final MajorQuestionRepositoryPort majorQuestionRepositoryPort;
    private final CheckAdminPolicy checkAdminPolicy;

    @Override
    @Transactional
    public void execute(DeleteMajorQuestionData.Command command) {
        checkAdminPolicy.check(command.actor());

        if (!majorQuestionRepositoryPort.findById(command.questionId()).isPresent()) {
            throw new MajorQuestionNotFoundException(command.questionId());
        }

        majorQuestionRepositoryPort.deleteById(command.questionId());
    }
}
