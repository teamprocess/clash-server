package com.process.clash.application.helpcontent.service;

import com.process.clash.application.common.policy.CheckAdminPolicy;
import com.process.clash.application.helpcontent.data.UpdateHelpContentData;
import com.process.clash.application.helpcontent.exception.exception.notfound.HelpContentNotFoundException;
import com.process.clash.application.helpcontent.port.in.UpdateHelpContentUseCase;
import com.process.clash.application.helpcontent.port.out.HelpContentRepositoryPort;
import com.process.clash.domain.helpcontent.entity.HelpContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateHelpContentService implements UpdateHelpContentUseCase {

    private final HelpContentRepositoryPort helpContentRepositoryPort;
    private final CheckAdminPolicy checkAdminPolicy;

    @Override
    public UpdateHelpContentData.Result execute(UpdateHelpContentData.Command command) {
        checkAdminPolicy.check(command.actor());

        HelpContent existing = helpContentRepositoryPort.findByKey(command.key())
                .orElseThrow(HelpContentNotFoundException::new);

        HelpContent updated = new HelpContent(
                existing.key(),
                command.content(),
                existing.version() + 1,
                existing.createdAt(),
                null
        );

        return UpdateHelpContentData.Result.from(helpContentRepositoryPort.save(updated));
    }
}
