package com.process.clash.application.user.userattendance.service;

import com.process.clash.application.user.userattendance.port.out.UserAttendanceRepositoryPort;
import com.process.clash.application.user.userattendance.realtime.AttendanceRefetchNotifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BroadcastAttendanceBoardService {

    private final UserAttendanceRepositoryPort userAttendanceRepositoryPort;
    private final AttendanceRefetchNotifier attendanceRefetchNotifier;

    /**
     * 전체 활성 유저에게 주간 출석판 갱신 알림을 소켓으로 전송한다.
     */
    public void broadcastAttendanceBoard() {
        List<Long> userIds = userAttendanceRepositoryPort.findAllNonDeletedUserIds();
        if (userIds.isEmpty()) {
            return;
        }
        attendanceRefetchNotifier.notifyAttendanceBoard(userIds);
    }
}