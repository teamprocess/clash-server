package com.process.clash.application.compete.rival.rival.policy;

import com.process.clash.application.compete.rival.rival.exception.exception.notfound.RivalNotFoundException;
import com.process.clash.application.compete.rival.rival.port.out.RivalRepositoryPort;
import com.process.clash.domain.rival.rival.entity.Rival;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemoveRivalPolicy {

    private final RivalRepositoryPort rivalRepositoryPort;

    public Rival check(Long id) {

        return rivalRepositoryPort.findById(id)
                .orElseThrow(RivalNotFoundException::new);
    }
}
