package com.process.clash.adapter.persistence.task;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskJpaRepository extends JpaRepository<TaskJpaEntity, Long> {

    @EntityGraph(attributePaths = "user")
    List<TaskJpaEntity> findAllByUserId(Long userId);
}
