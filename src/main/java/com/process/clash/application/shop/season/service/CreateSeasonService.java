package com.process.clash.application.shop.season.service;

import com.process.clash.application.shop.season.data.CreateSeasonData;
import com.process.clash.application.shop.season.port.in.CreateSeasonUseCase;
import com.process.clash.application.shop.season.port.out.SeasonRepositoryPort;
import com.process.clash.domain.common.policy.CheckAdminPolicy;
import com.process.clash.domain.shop.season.entity.Season;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateSeasonService implements CreateSeasonUseCase {

    private final SeasonRepositoryPort seasonRepositoryPort;
    private final CheckAdminPolicy checkAdminPolicy;

    @Override
    public void execute(CreateSeasonData.Command command) {
        checkAdminPolicy.check(command.actor());

        Season season = Season.createDefault(command.title(), command.startDate(), command.endDate());
        seasonRepositoryPort.save(season);
    }
}
