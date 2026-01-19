package com.process.clash.adapter.persistence.githubinfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface GitHubInfoJpaRepository extends JpaRepository<GitHubInfoJpaEntity, Long> {

    Optional<GitHubInfoJpaEntity> findByUserIdAndDate(Long userId, LocalDate date);
}
