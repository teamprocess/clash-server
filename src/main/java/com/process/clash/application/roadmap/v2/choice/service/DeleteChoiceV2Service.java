package com.process.clash.application.roadmap.v2.choice.service;

import com.process.clash.application.roadmap.v2.choice.data.DeleteChoiceV2Data;
import com.process.clash.application.roadmap.v2.choice.port.in.DeleteChoiceV2UseCase;
import com.process.clash.application.roadmap.v2.port.out.ChoiceV2RepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteChoiceV2Service implements DeleteChoiceV2UseCase {

    private final ChoiceV2RepositoryPort choiceV2RepositoryPort;

    @Override
    @Transactional
    public void execute(DeleteChoiceV2Data.Command command) {
        choiceV2RepositoryPort.deleteById(command.choiceId());
    }
}
