package com.process.clash.application.helpcontent.service;

import com.process.clash.application.common.policy.CheckAdminPolicy;
import com.process.clash.application.helpcontent.data.CreateHelpContentData;
import com.process.clash.application.helpcontent.exception.exception.conflict.HelpContentAlreadyExistsException;
import com.process.clash.application.helpcontent.port.in.CreateHelpContentUseCase;
import com.process.clash.application.helpcontent.port.out.HelpContentRepositoryPort;
import com.process.clash.domain.helpcontent.entity.HelpContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateHelpContentService implements CreateHelpContentUseCase {

    private final HelpContentRepositoryPort helpContentRepositoryPort;
    private final CheckAdminPolicy checkAdminPolicy;

    @Override
    public CreateHelpContentData.Result execute(CreateHelpContentData.Command command) {
        checkAdminPolicy.check(command.actor());

        if (helpContentRepositoryPort.existsByKey(command.key())) {
            throw new HelpContentAlreadyExistsException();
        }

        HelpContent saved = helpContentRepositoryPort.save(command.toDomain());
        return CreateHelpContentData.Result.from(saved);
    }
}
