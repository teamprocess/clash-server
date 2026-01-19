package com.process.clash.adapter.persistence.user.usergithub;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGitHubJpaRepository extends JpaRepository<UserGitHubJpaEntity, Long> {

    List<UserGitHubJpaEntity> findByUserIdNotIn(List<Long> ids);

    @Query("select ug from UserGitHubJpaEntity ug join fetch ug.user where ug.gitHubId is not null")
    List<UserGitHubJpaEntity> findAllWithUserAndGitHubId();
}
