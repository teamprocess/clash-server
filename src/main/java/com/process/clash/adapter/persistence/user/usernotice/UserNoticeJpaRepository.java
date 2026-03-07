package com.process.clash.adapter.persistence.user.usernotice;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNoticeJpaRepository extends JpaRepository<UserNoticeJpaEntity, Long> {

    @Query("""
        select un
        from UserNoticeJpaEntity un
        join fetch un.sender
        join fetch un.receiver
        where un.receiver.id = :receiverId
            and un.isRead = false
        order by un.createdAt desc
    """)
    List<UserNoticeJpaEntity> findAllByReceiver_IdOrderByCreatedAtDesc(@Param("receiverId") Long receiverId);

    Optional<UserNoticeJpaEntity> findByIdAndReceiver_Id(Long id, Long receiverId);

    @Modifying
    @Query(value = "UPDATE user_notices SET deleted_at = now() WHERE rival_id = :rivalId AND notice_category = 'APPLY_RIVAL' AND deleted_at IS NULL", nativeQuery = true)
    void softDeleteApplyRivalNoticeByRivalId(@Param("rivalId") Long rivalId);
}
