package com.process.clash.application.shop.season.port.out;

import com.process.clash.domain.shop.season.entity.Season;
import org.springframework.stereotype.Component;

@Component
public interface SeasonRepositoryPort {
    Season save(Season season);
}
