package com.process.clash.adapter.persistence.user.github.usergithubinfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGitHubJpaRepository extends JpaRepository<UserGitHubInfoJpaEntity, Long> {
}
