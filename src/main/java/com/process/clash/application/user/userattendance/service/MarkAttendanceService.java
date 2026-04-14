package com.process.clash.application.user.userattendance.service;

import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.userattendance.data.MarkAttendanceData;
import com.process.clash.application.user.userattendance.exception.exception.conflict.AlreadyAttendedException;
import com.process.clash.application.user.userattendance.exception.exception.notfound.UserAttendanceNotFoundException;
import com.process.clash.application.user.userattendance.port.in.MarkAttendanceUseCase;
import com.process.clash.application.user.userattendance.port.out.UserAttendanceRepositoryPort;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.userattendance.entity.UserAttendance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class MarkAttendanceService implements MarkAttendanceUseCase {

    private final UserAttendanceRepositoryPort userAttendanceRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public MarkAttendanceData.Result execute(MarkAttendanceData.Command command) {
        Long userId = command.actor().id();
        LocalDate attendanceDate = UserAttendance.currentAttendanceDate();

        UserAttendance attendance = userAttendanceRepositoryPort
                .findByUserIdAndAttendanceDate(userId, attendanceDate)
                .orElseThrow(UserAttendanceNotFoundException::new);

        if (attendance.isAttended()) {
            throw new AlreadyAttendedException();
        }

        userAttendanceRepositoryPort.save(attendance.markAttend());

        User user = userRepositoryPort.findByIdForUpdate(userId)
                .orElseThrow(UserNotFoundException::new);

        User updated = user.incrementAttendanceStreak();
        userRepositoryPort.save(updated);

        return new MarkAttendanceData.Result(updated.currentAttendanceStreak());
    }
}