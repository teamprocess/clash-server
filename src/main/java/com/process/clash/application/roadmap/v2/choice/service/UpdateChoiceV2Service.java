package com.process.clash.application.roadmap.v2.choice.service;

import com.process.clash.application.roadmap.v2.choice.data.UpdateChoiceV2Data;
import com.process.clash.application.roadmap.v2.choice.port.in.UpdateChoiceV2UseCase;
import com.process.clash.application.roadmap.v2.port.out.ChoiceV2RepositoryPort;
import com.process.clash.application.roadmap.v2.question.exception.exception.notfound.ChoiceV2NotFoundException;
import com.process.clash.domain.roadmap.v2.entity.ChoiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateChoiceV2Service implements UpdateChoiceV2UseCase {

    private final ChoiceV2RepositoryPort choiceV2RepositoryPort;

    @Override
    @Transactional
    public UpdateChoiceV2Data.Result execute(UpdateChoiceV2Data.Command command) {
        ChoiceV2 choice = choiceV2RepositoryPort.findById(command.choiceId())
                .orElseThrow(ChoiceV2NotFoundException::new);

        ChoiceV2 updatedChoice = new ChoiceV2(
                choice.getId(),
                choice.getQuestionId(),
                command.content(),
                command.isCorrect(),
                command.orderIndex()
        );

        ChoiceV2 savedChoice = choiceV2RepositoryPort.save(updatedChoice);
        return UpdateChoiceV2Data.Result.from(savedChoice);
    }
}
