package com.process.clash.adapter.persistence.group;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMemberJpaRepository extends JpaRepository<GroupMemberJpaEntity, Long> {

    boolean existsByGroupIdAndUserId(Long groupId, Long userId);

    int countByGroupId(Long groupId);

    void deleteByGroupIdAndUserId(Long groupId, Long userId);

    void deleteByGroupId(Long groupId);

    @EntityGraph(attributePaths = {"user"})
    List<GroupMemberJpaEntity> findAllByGroupId(Long groupId);

    @EntityGraph(attributePaths = {"user"})
    Page<GroupMemberJpaEntity> findAllByGroupId(Long groupId, Pageable pageable);

    @Query("""
        select gm.group.id
        from GroupMemberJpaEntity gm
        where gm.user.id = :userId
            and gm.group.id in :groupIds
    """)
    List<Long> findGroupIdsByUserIdAndGroupIds(
        @Param("userId") Long userId,
        @Param("groupIds") List<Long> groupIds
    );

    @Query("""
        select gm.group.id as groupId, count(gm.id) as memberCount
        from GroupMemberJpaEntity gm
        where gm.group.id in :groupIds
        group by gm.group.id
    """)
    List<GroupMemberCountProjection> countAllByGroupIds(@Param("groupIds") List<Long> groupIds);

    interface GroupMemberCountProjection {
        Long getGroupId();
        Long getMemberCount();
    }
}
