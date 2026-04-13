package com.process.clash.adapter.scheduler;

import com.process.clash.application.user.userattendance.service.InitUserAttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserAttendanceInitScheduler {

    private final InitUserAttendanceService initUserAttendanceService;

    /**
     * 매일 KST 06:00에 전체 활성 유저의 출석 기록을 초기화 (is_attended = false)
     */
    @Scheduled(cron = "0 0 6 * * *", zone = "Asia/Seoul")
    public void initDailyAttendance() {
        try {
            log.info("출석 초기화 스케줄 실행 시작");
            initUserAttendanceService.initDailyAttendance();
            log.info("출석 초기화 스케줄 실행 완료");
        } catch (Exception e) {
            log.error("출석 초기화 스케줄 실행 실패", e);
        }
    }
}