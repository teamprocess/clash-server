package com.process.clash.application.user.userattendance.service;

import com.process.clash.application.user.userattendance.port.out.UserAttendanceRepositoryPort;
import com.process.clash.domain.user.userattendance.entity.UserAttendance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    @DisplayName("기존 출석 레코드를 전부 삭제한 후 재생성한다")
    void initDailyAttendance_deletesBeforeSaving() {
        when(userAttendanceRepositoryPort.findAllNonDeletedUserIds()).thenReturn(List.of(1L));

        initUserAttendanceService.initDailyAttendance();

        verify(userAttendanceRepositoryPort).deleteAll();
        verify(userAttendanceRepositoryPort).saveAll(org.mockito.ArgumentMatchers.anyList());
    }

    @Test
    @DisplayName("모든 탈퇴하지 않은 유저에 대해 isAttended=false 레코드를 생성한다")
    void initDailyAttendance_createsNotAttendedRecordForAllUsers() {
        List<Long> userIds = List.of(1L, 2L, 3L);
        when(userAttendanceRepositoryPort.findAllNonDeletedUserIds()).thenReturn(userIds);

        initUserAttendanceService.initDailyAttendance();

        ArgumentCaptor<List<UserAttendance>> captor = ArgumentCaptor.forClass(List.class);
        verify(userAttendanceRepositoryPort).saveAll(captor.capture());

        List<UserAttendance> saved = captor.getValue();
        assertThat(saved).hasSize(3);
        assertThat(saved).allMatch(a -> !a.isAttended());
        assertThat(saved).extracting(UserAttendance::userId)
                .containsExactlyInAnyOrder(1L, 2L, 3L);
    }

    @Test
    @DisplayName("모든 레코드의 attendanceDate는 currentAttendanceDate와 동일하다")
    void initDailyAttendance_setsCorrectAttendanceDate() {
        when(userAttendanceRepositoryPort.findAllNonDeletedUserIds()).thenReturn(List.of(1L, 2L));

        initUserAttendanceService.initDailyAttendance();

        ArgumentCaptor<List<UserAttendance>> captor = ArgumentCaptor.forClass(List.class);
        verify(userAttendanceRepositoryPort).saveAll(captor.capture());

        captor.getValue().forEach(a ->
                assertThat(a.attendanceDate()).isEqualTo(UserAttendance.currentAttendanceDate())
        );
    }

    @Test
    @DisplayName("탈퇴하지 않은 유저가 없으면 빈 목록으로 saveAll을 호출한다")
    void initDailyAttendance_savesEmptyList_whenNoUsers() {
        when(userAttendanceRepositoryPort.findAllNonDeletedUserIds()).thenReturn(List.of());

        initUserAttendanceService.initDailyAttendance();

        verify(userAttendanceRepositoryPort).deleteAll();
        verify(userAttendanceRepositoryPort).saveAll(List.of());
    }
}