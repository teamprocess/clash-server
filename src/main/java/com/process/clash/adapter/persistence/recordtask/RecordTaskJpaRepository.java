package com.process.clash.adapter.persistence.recordtask;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordTaskJpaRepository extends JpaRepository<RecordTaskJpaEntity, Long> {

    @EntityGraph(attributePaths = "user")
    List<RecordTaskJpaEntity> findAllByUserId(Long userId);
}
