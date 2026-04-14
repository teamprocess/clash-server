package com.process.clash.application.user.userattendance.port.out;

import com.process.clash.domain.user.userattendance.entity.UserAttendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserAttendanceRepositoryPort {

    UserAttendance save(UserAttendance userAttendance);

    Optional<UserAttendance> findByUserIdAndAttendanceDate(Long userId, LocalDate attendanceDate);

    List<Long> findAllNonDeletedUserIds();

    void saveAll(List<UserAttendance> attendances);

    void deleteAll();

    List<Long> findNotAttendedUserIdsByDate(LocalDate date);

    long countAttendedByUserIdBetween(Long userId, LocalDate start, LocalDate end);
}