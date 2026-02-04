package com.process.clash.application.roadmap.v2.choice.service;

import com.process.clash.application.roadmap.v2.choice.data.CreateChoiceV2Data;
import com.process.clash.application.roadmap.v2.choice.port.in.CreateChoiceV2UseCase;
import com.process.clash.application.roadmap.v2.port.out.ChoiceV2RepositoryPort;
import com.process.clash.domain.roadmap.v2.entity.ChoiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateChoiceV2Service implements CreateChoiceV2UseCase {

    private final ChoiceV2RepositoryPort choiceV2RepositoryPort;

    @Override
    @Transactional
    public CreateChoiceV2Data.Result execute(CreateChoiceV2Data.Command command) {
        ChoiceV2 choice = command.toDomain();
        ChoiceV2 savedChoice = choiceV2RepositoryPort.save(choice);
        return CreateChoiceV2Data.Result.from(savedChoice);
    }
}
