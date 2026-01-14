package com.process.clash.application.shop.season.port.out;

import com.process.clash.domain.shop.season.entity.Season;

public interface SeasonRepositoryPort {
    Season save(Season season);
}
