package com.process.clash.adapter.persistence.shop.season;

import com.process.clash.application.shop.season.port.out.SeasonRepositoryPort;
import com.process.clash.domain.shop.season.entity.Season;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SeasonPersistenceAdapter implements SeasonRepositoryPort {

    private final SeasonJpaRepository seasonJpaRepository;
    private final SeasonJpaMapper seasonJpaMapper;

    @Override
    public Season save(Season season) {
        SeasonJpaEntity seasonJpaEntity = seasonJpaMapper.toJpaEntity(season);
        SeasonJpaEntity savedEntity = seasonJpaRepository.save(seasonJpaEntity);
        return seasonJpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Season> findById(Long seasonId) {
        return seasonJpaRepository.findById(seasonId)
                .map(seasonJpaMapper::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return seasonJpaRepository.existsByName(name);
    }
}
