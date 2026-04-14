package com.process.clash.adapter.persistence.user.userattendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserAttendanceJpaRepository extends JpaRepository<UserAttendanceJpaEntity, Long> {

    @Query("SELECT ua FROM UserAttendanceJpaEntity ua WHERE ua.user.id = :userId AND ua.attendanceDate = :attendanceDate")
    Optional<UserAttendanceJpaEntity> findByUserIdAndAttendanceDate(
            @Param("userId") Long userId,
            @Param("attendanceDate") LocalDate attendanceDate
    );

    @Query(value = """
        SELECT u.id
        FROM users u
        WHERE u.deleted_at IS NULL
    """, nativeQuery = true)
    List<Long> findAllNonDeletedUserIds();
}