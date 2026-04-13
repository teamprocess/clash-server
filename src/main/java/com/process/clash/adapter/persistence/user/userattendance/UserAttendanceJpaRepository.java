package com.process.clash.adapter.persistence.user.userattendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAttendanceJpaRepository extends JpaRepository<UserAttendanceJpaEntity, Long> {

    Optional<UserAttendanceJpaEntity> findByUserIdAndIsAttended(Long userId, boolean isAttended);

    boolean existsByUserIdAndIsAttended(Long userId, boolean isAttended);
}