package com.process.clash.application.user.userattendance.port.out;

import com.process.clash.domain.user.userattendance.entity.UserAttendance;

import java.util.List;
import java.util.Optional;

public interface UserAttendanceRepositoryPort {

    UserAttendance save(UserAttendance userAttendance);

    Optional<UserAttendance> findByUserIdAndIsAttended(Long userId, boolean isAttended);

    boolean existsByUserIdAndIsAttended(Long userId, boolean isAttended);

    List<Long> findAllNonDeletedUserIds();

    void saveAll(List<UserAttendance> attendances);

    void deleteAll();
}