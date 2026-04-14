package com.process.clash.application.user.userattendance.service;

import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.userattendance.port.out.UserAttendanceRepositoryPort;
import com.process.clash.domain.user.userattendance.entity.UserAttendance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResetAttendanceStreakService {

    private final UserAttendanceRepositoryPort userAttendanceRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    /**
     * 전날 출석하지 않은 유저들의 연속 출석 streak을 0으로 초기화한다.
     * KST 06:00 기준으로 하루가 바뀌므로, currentAttendanceDate()에서 하루를 빼면 전날 날짜가 된다.
     */
    @Transactional
    public void resetStreakForMissedAttendance() {
        LocalDate yesterday = UserAttendance.currentAttendanceDate().minusDays(1);
        List<Long> missedUserIds = userAttendanceRepositoryPort.findNotAttendedUserIdsByDate(yesterday);

        if (missedUserIds.isEmpty()) {
            return;
        }

        userRepositoryPort.resetAttendanceStreakByIds(missedUserIds);
    }
}