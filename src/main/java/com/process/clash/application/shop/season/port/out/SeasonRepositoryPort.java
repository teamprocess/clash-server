package com.process.clash.application.shop.season.port.out;

import com.process.clash.domain.shop.season.entity.Season;

import java.util.Optional;

public interface SeasonRepositoryPort {
    Season save(Season season);
    Optional<Season> findById(Long seasonId);
    boolean existsByName(String name);
}
