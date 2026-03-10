package com.process.clash.adapter.persistence.shop.season;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SeasonJpaRepository extends JpaRepository<SeasonJpaEntity, Long> {
    boolean existsByName(String name);

    @Query("SELECT s FROM SeasonJpaEntity s WHERE :date BETWEEN s.startDate AND s.endDate")
    Optional<SeasonJpaEntity> findCurrentSeason(@Param("date") LocalDate date);
}
