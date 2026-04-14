package com.process.clash.application.user.userattendance.service;

import com.process.clash.application.user.userattendance.port.out.UserAttendanceRepositoryPort;
import com.process.clash.domain.user.userattendance.entity.UserAttendance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InitUserAttendanceService {

    private final UserAttendanceRepositoryPort userAttendanceRepositoryPort;

    @Transactional
    public void initDailyAttendance() {
        LocalDate attendanceDate = UserAttendance.currentAttendanceDate();

        userAttendanceRepositoryPort.deleteAll();

        List<Long> userIds = userAttendanceRepositoryPort.findAllNonDeletedUserIds();

        List<UserAttendance> attendances = userIds.stream()
                .map(userId -> UserAttendance.create(userId, attendanceDate))
                .toList();

        userAttendanceRepositoryPort.saveAll(attendances);
    }
}