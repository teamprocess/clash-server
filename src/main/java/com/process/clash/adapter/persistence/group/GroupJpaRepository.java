package com.process.clash.adapter.persistence.group;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupJpaRepository extends JpaRepository<GroupJpaEntity, Long> {

    @EntityGraph(attributePaths = {"owner"})
    Page<GroupJpaEntity> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"owner"})
    Optional<GroupJpaEntity> findById(Long id);

    @EntityGraph(attributePaths = {"owner"})
    @Query("""
        select g
        from GroupJpaEntity g
        join GroupMemberJpaEntity gm on gm.group = g
        where gm.user.id = :userId
    """)
    Page<GroupJpaEntity> findAllByMemberUserId(@Param("userId") Long userId, Pageable pageable);
}
