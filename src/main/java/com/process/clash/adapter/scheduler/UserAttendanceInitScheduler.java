package com.process.clash.adapter.scheduler;

import com.process.clash.application.user.userattendance.service.InitUserAttendanceService;
import com.process.clash.application.user.userattendance.service.ResetAttendanceStreakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserAttendanceInitScheduler {

    private final InitUserAttendanceService initUserAttendanceService;
    private final ResetAttendanceStreakService resetAttendanceStreakService;

    /**
     * 매일 KST 06:00에 다음 두 작업을 순서대로 수행한다.
     * 1. 전날 출석하지 않은 유저들의 연속 출석 streak을 0으로 초기화
     * 2. 전체 활성 유저의 출석 기록을 오늘 날짜 기준으로 새로 생성 (is_attended = false)
     *
     * streak 초기화가 반드시 deleteAll() 이전에 실행되어야 하므로 같은 스케줄 메서드 안에서 순서를 보장한다.
     */
    @Scheduled(cron = "0 0 6 * * *", zone = "Asia/Seoul")
    public void initDailyAttendance() {
        try {
            log.info("연속 출석 streak 초기화 스케줄 실행 시작");
            resetAttendanceStreakService.resetStreakForMissedAttendance();
            log.info("연속 출석 streak 초기화 스케줄 실행 완료");
        } catch (Exception e) {
            log.error("연속 출석 streak 초기화 스케줄 실행 실패", e);
        }

        try {
            log.info("출석 초기화 스케줄 실행 시작");
            initUserAttendanceService.initDailyAttendance();
            log.info("출석 초기화 스케줄 실행 완료");
        } catch (Exception e) {
            log.error("출석 초기화 스케줄 실행 실패", e);
        }
    }
}