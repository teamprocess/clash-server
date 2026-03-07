package com.process.clash.adapter.persistence.user.usernotice;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
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

    @Query("""
        select un
        from UserNoticeJpaEntity un
        join fetch un.sender
        join fetch un.receiver
        where un.receiver.id = :receiverId
        order by un.createdAt desc
    """)
    List<UserNoticeJpaEntity> findAllByReceiver_IdOrderByCreatedAtDescIncludingRead(@Param("receiverId") Long receiverId);

    Optional<UserNoticeJpaEntity> findByIdAndReceiver_Id(Long id, Long receiverId);
}
