package com.process.clash.application.user.userattendance.service;

import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.userattendance.port.out.UserAttendanceRepositoryPort;
import com.process.clash.application.user.userattendance.realtime.AttendanceRefetchNotifier;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import com.process.clash.domain.user.userattendance.entity.UserAttendance;
import com.process.clash.domain.user.userrankhistory.enums.ExpTier;
import com.process.clash.domain.user.userrankhistory.enums.RankTier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResetAttendanceStreakServiceTest {

    @Mock
    private UserAttendanceRepositoryPort userAttendanceRepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private AttendanceRefetchNotifier attendanceRefetchNotifier;

    private ResetAttendanceStreakService resetAttendanceStreakService;

    private final LocalDate YESTERDAY = UserAttendance.currentAttendanceDate().minusDays(1);

    @BeforeEach
    void setUp() {
        resetAttendanceStreakService = new ResetAttendanceStreakService(
                userAttendanceRepositoryPort,
                userRepositoryPort,
                attendanceRefetchNotifier
        );
    }

    @Test
    @DisplayName("전날 미출석 유저가 없으면 saveAll과 소켓 알림을 호출하지 않는다")
    void resetStreakForMissedAttendance_doesNothing_whenNoMissedUsers() {
        when(userAttendanceRepositoryPort.findNotAttendedUserIdsByDate(YESTERDAY))
                .thenReturn(List.of());

        resetAttendanceStreakService.resetStreakForMissedAttendance();

        verify(userRepositoryPort, never()).findAllByIds(any());
        verify(userRepositoryPort, never()).saveAll(any());
        verify(attendanceRefetchNotifier, never()).notifyAttendanceMissed(any());
    }

    @Test
    @DisplayName("전날 미출석 유저들의 streak이 모두 0으로 저장된다")
    void resetStreakForMissedAttendance_resetsStreakToZero_forMissedUsers() {
        List<Long> missedIds = List.of(1L, 2L);
        when(userAttendanceRepositoryPort.findNotAttendedUserIdsByDate(YESTERDAY))
                .thenReturn(missedIds);
        when(userRepositoryPort.findAllByIds(missedIds))
                .thenReturn(List.of(createUser(1L, 5), createUser(2L, 3)));

        resetAttendanceStreakService.resetStreakForMissedAttendance();

        ArgumentCaptor<List<User>> captor = ArgumentCaptor.forClass(List.class);
        verify(userRepositoryPort).saveAll(captor.capture());

        assertThat(captor.getValue())
                .allMatch(u -> u.currentAttendanceStreak() == 0);
    }

    @Test
    @DisplayName("미출석 유저 수만큼 saveAll에 전달된다")
    void resetStreakForMissedAttendance_savesAllMissedUsers() {
        List<Long> missedIds = List.of(1L, 2L, 3L);
        when(userAttendanceRepositoryPort.findNotAttendedUserIdsByDate(YESTERDAY))
                .thenReturn(missedIds);
        when(userRepositoryPort.findAllByIds(missedIds))
                .thenReturn(List.of(createUser(1L, 7), createUser(2L, 1), createUser(3L, 10)));

        resetAttendanceStreakService.resetStreakForMissedAttendance();

        ArgumentCaptor<List<User>> captor = ArgumentCaptor.forClass(List.class);
        verify(userRepositoryPort).saveAll(captor.capture());

        assertThat(captor.getValue()).hasSize(3);
    }

    @Test
    @DisplayName("어제 날짜(currentAttendanceDate - 1)로 미출석 유저를 조회한다")
    void resetStreakForMissedAttendance_queriesYesterdayDate() {
        when(userAttendanceRepositoryPort.findNotAttendedUserIdsByDate(any()))
                .thenReturn(List.of());

        resetAttendanceStreakService.resetStreakForMissedAttendance();

        ArgumentCaptor<LocalDate> dateCaptor = ArgumentCaptor.forClass(LocalDate.class);
        verify(userAttendanceRepositoryPort).findNotAttendedUserIdsByDate(dateCaptor.capture());

        assertThat(dateCaptor.getValue())
                .isEqualTo(UserAttendance.currentAttendanceDate().minusDays(1));
    }

    @Test
    @DisplayName("미출석 유저가 있으면 streak 초기화 후 소켓 알림을 발송한다")
    void resetStreakForMissedAttendance_notifiesViaSocket_whenMissedUsersExist() {
        List<Long> missedIds = List.of(1L, 2L);
        when(userAttendanceRepositoryPort.findNotAttendedUserIdsByDate(YESTERDAY))
                .thenReturn(missedIds);
        when(userRepositoryPort.findAllByIds(missedIds))
                .thenReturn(List.of(createUser(1L, 3), createUser(2L, 5)));

        resetAttendanceStreakService.resetStreakForMissedAttendance();

        verify(attendanceRefetchNotifier).notifyAttendanceMissed(missedIds);
    }

    private User createUser(Long id, int attendanceStreak) {
        return new User(
                id, Instant.now(), Instant.now(),
                "username" + id, "user" + id + "@example.com", "name", "encoded-password",
                Role.USER, "", 0, 0, Major.NONE, UserStatus.ACTIVE,
                null, false, RankTier.NONE, ExpTier.UNRANKED,
                attendanceStreak
        );
    }
}