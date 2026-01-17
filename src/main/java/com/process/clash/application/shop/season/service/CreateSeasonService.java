package com.process.clash.application.shop.season.service;

import com.process.clash.application.shop.season.data.CreateSeasonData;
import com.process.clash.application.shop.season.exception.exception.conflict.SeasonAlreadyExistsException;
import com.process.clash.application.shop.season.port.in.CreateSeasonUseCase;
import com.process.clash.application.shop.season.port.out.SeasonRepositoryPort;
import com.process.clash.domain.common.policy.CheckAdminPolicy;
import com.process.clash.domain.shop.season.entity.Season;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateSeasonService implements CreateSeasonUseCase {

    private final SeasonRepositoryPort seasonRepositoryPort;
    private final CheckAdminPolicy checkAdminPolicy;

    @Override
    public void execute(CreateSeasonData.Command command) {

        checkAdminPolicy.check(command.actor());

        if (seasonRepositoryPort.existsByName(command.name())) {
            throw new SeasonAlreadyExistsException();
        }

        Season season = Season.createDefault(command.name(), command.startDate(), command.endDate());
        seasonRepositoryPort.save(season);
    }
}
