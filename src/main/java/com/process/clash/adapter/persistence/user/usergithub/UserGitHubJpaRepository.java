package com.process.clash.adapter.persistence.user.usergithub;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGitHubJpaRepository extends JpaRepository<UserGitHubJpaEntity, Long> {

    List<UserGitHubJpaEntity> findByUser_IdNotIn(List<Long> ids);
}
