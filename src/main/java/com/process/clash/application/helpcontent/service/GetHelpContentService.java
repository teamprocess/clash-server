package com.process.clash.application.helpcontent.service;

import com.process.clash.application.helpcontent.data.GetHelpContentData;
import com.process.clash.application.helpcontent.exception.exception.notfound.HelpContentNotFoundException;
import com.process.clash.application.helpcontent.port.in.GetHelpContentUseCase;
import com.process.clash.application.helpcontent.port.out.HelpContentRepositoryPort;
import com.process.clash.domain.helpcontent.entity.HelpContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetHelpContentService implements GetHelpContentUseCase {

    private final HelpContentRepositoryPort helpContentRepositoryPort;

    @Override
    public GetHelpContentData.Result execute(String key) {
        HelpContent helpContent = helpContentRepositoryPort.findByKey(key)
                .orElseThrow(HelpContentNotFoundException::new);
        return GetHelpContentData.Result.from(helpContent);
    }
}
