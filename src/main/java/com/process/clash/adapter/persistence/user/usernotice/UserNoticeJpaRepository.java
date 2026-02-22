package com.process.clash.adapter.persistence.user.usernotice;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNoticeJpaRepository extends JpaRepository<UserNoticeJpaEntity, Long> {

    List<UserNoticeJpaEntity> findAllByReceiver_IdOrderByCreatedAtDesc(Long receiverId);

    Optional<UserNoticeJpaEntity> findByIdAndReceiver_Id(Long id, Long receiverId);
}
