package com.process.clash.adapter.scheduler;

import com.process.clash.application.user.userattendance.service.BroadcastAttendanceBoardService;
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
    private final BroadcastAttendanceBoardService broadcastAttendanceBoardService;

    /**
     * 매일 KST 06:00에 다음 두 작업을 순서대로 수행한다.
     * 1. 전날 출석하지 않은 유저들의 streak 초기화 + 소켓 알림 발송
     * 2. 전체 활성 유저의 오늘 출석 레코드 생성 (이미 존재하면 무시)
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

    /**
     * 매일 KST 07:00 ~ 05:00 매 정시마다 전체 활성 유저에게 주간 출석판 갱신 알림을 전송한다.
     * 06:00 이후에 앱을 실행한 유저도 최대 1시간 이내에 출석판을 확인할 수 있도록 한다.
     */
    @Scheduled(cron = "0 0 0-5,7-23 * * *", zone = "Asia/Seoul")
    public void broadcastAttendanceBoard() {
        try {
            log.info("출석판 브로드캐스트 스케줄 실행 시작");
            broadcastAttendanceBoardService.broadcastAttendanceBoard();
            log.info("출석판 브로드캐스트 스케줄 실행 완료");
        } catch (Exception e) {
            log.error("출석판 브로드캐스트 스케줄 실행 실패", e);
        }
    }
}