package com.process.clash.adapter.persistence.shop.season;

import com.process.clash.domain.shop.season.entity.Season;
import org.springframework.stereotype.Component;

@Component
public class SeasonJpaMapper {

    public SeasonJpaEntity toJpaEntity(Season season) {
        return new SeasonJpaEntity(
                season.id(),
                season.createdAt(),
                season.updatedAt(),
                season.name(),
                season.startDate(),
                season.endDate()
        );
    }

    public Season toDomain(SeasonJpaEntity seasonJpaEntity) {
        return new Season(
                seasonJpaEntity.getId(),
                seasonJpaEntity.getCreatedAt(),
                seasonJpaEntity.getUpdatedAt(),
                seasonJpaEntity.getName(),
                seasonJpaEntity.getStartDate(),
                seasonJpaEntity.getEndDate()
        );
    }
}