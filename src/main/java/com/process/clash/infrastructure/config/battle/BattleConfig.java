package com.process.clash.infrastructure.config.battle;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;

@Configuration
public class BattleConfig {

    @Bean
    public ZoneId battleZoneId(@Value("${battle.timezone:Asia/Seoul}") String battleTimezone) {
        return ZoneId.of(battleTimezone);
    }
}
