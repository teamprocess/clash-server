package com.process.clash.adapter.persistence.github;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GitHubJpaRepository extends JpaRepository<GitHubJpaEntity, Long> {
}
