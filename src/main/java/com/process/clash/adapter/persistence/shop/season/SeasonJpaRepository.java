package com.process.clash.adapter.persistence.shop.season;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonJpaRepository extends JpaRepository<SeasonJpaEntity, Long> {
    boolean existsByName(String name);
}
