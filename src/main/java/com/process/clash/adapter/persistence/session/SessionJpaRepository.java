package com.process.clash.adapter.persistence.session;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionJpaRepository extends JpaRepository<SessionJpaEntity, Long> {

    @EntityGraph(attributePaths = {"user", "task"})
    List<SessionJpaEntity> findAllByUserId(Long userId);
}
