package com.process.clash.application.user.userattendance.service;

import com.process.clash.application.user.userattendance.port.out.UserAttendanceRepositoryPort;
import com.process.clash.domain.user.userattendance.entity.UserAttendance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InitUserAttendanceServiceTest {

    @Mock
    private UserAttendanceRepositoryPort userAttendanceRepositoryPort;

    private InitUserAttendanceService initUserAttendanceService;

    @BeforeEach
    void setUp() {
        initUserAttendanceService = new InitUserAttendanceService(userAttendanceRepositoryPort);
    }

    @Test
    @DisplayName("오늘 날짜로 모든 활성 유저의 출석 레코드를 생성한다 (이미 존재하면 무시)")
    void initDailyAttendance_insertsForAllActiveUsers() {
        initUserAttendanceService.initDailyAttendance();

        verify(userAttendanceRepositoryPort).initDailyAttendanceForAllUsers(
                eq(UserAttendance.currentAttendanceDate())
        );
    }
}