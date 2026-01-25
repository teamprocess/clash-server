package com.process.clash.adapter.persistence.roadmap.sectionprogress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSectionProgressJpaRepository extends JpaRepository<UserSectionProgressJpaEntity, Long> {
    Optional<UserSectionProgressJpaEntity> findByUserIdAndSectionId(Long userId, Long sectionId);
    List<UserSectionProgressJpaEntity> findAllByUserId(Long userId);
    List<UserSectionProgressJpaEntity> findAllByUserIdAndSectionIdIn(Long userId, List<Long> sectionIds);

    @Query(value = """
    SELECT userId, userName, profileImage, totalCompleted, userRank
    FROM (
        SELECT 
            u.id as userId, 
            u.name as userName, 
            u.profile_image as profileImage,
            SUM(usp.completed_chapters) as totalCompleted,
            RANK() OVER (ORDER BY SUM(usp.completed_chapters) DESC) as userRank
        FROM users u
        JOIN user_section_progress usp ON u.id = usp.fk_user_id
        GROUP BY u.id
    ) rankTable
    WHERE userId = :targetUserId OR userRank <= 20
    ORDER BY userRank ASC
    """, nativeQuery = true)
    List<Object[]> findRankingsWithMyRank(@Param("targetUserId") Long targetUserId);
}

