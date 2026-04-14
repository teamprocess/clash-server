package com.process.clash.application.user.userattendance.service;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.userattendance.data.MarkAttendanceData;
import com.process.clash.application.user.userattendance.exception.exception.conflict.AlreadyAttendedException;
import com.process.clash.application.user.userattendance.exception.exception.notfound.UserAttendanceNotFoundException;
import com.process.clash.application.user.userattendance.port.out.UserAttendanceRepositoryPort;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MarkAttendanceServiceTest {

    @Mock
    private UserAttendanceRepositoryPort userAttendanceRepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    private MarkAttendanceService markAttendanceService;

    private final Long USER_ID = 1L;
    private final LocalDate ATTENDANCE_DATE = UserAttendance.currentAttendanceDate();

    @BeforeEach
    void setUp() {
        markAttendanceService = new MarkAttendanceService(userAttendanceRepositoryPort, userRepositoryPort);
    }

    @Test
    @DisplayName("오늘 출석 레코드가 없으면 UserAttendanceNotFoundException을 던진다")
    void execute_throwsUserAttendanceNotFoundException_whenNoRecord() {
        MarkAttendanceData.Command command = new MarkAttendanceData.Command(new Actor(USER_ID));
        when(userAttendanceRepositoryPort.findByUserIdAndAttendanceDate(USER_ID, ATTENDANCE_DATE))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> markAttendanceService.execute(command))
                .isInstanceOf(UserAttendanceNotFoundException.class);
    }

    @Test
    @DisplayName("이미 출석한 경우 AlreadyAttendedException을 던진다")
    void execute_throwsAlreadyAttendedException_whenAlreadyAttended() {
        MarkAttendanceData.Command command = new MarkAttendanceData.Command(new Actor(USER_ID));
        UserAttendance alreadyAttended = createAttendance(true);
        when(userAttendanceRepositoryPort.findByUserIdAndAttendanceDate(USER_ID, ATTENDANCE_DATE))
                .thenReturn(Optional.of(alreadyAttended));

        assertThatThrownBy(() -> markAttendanceService.execute(command))
                .isInstanceOf(AlreadyAttendedException.class);
    }

    @Test
    @DisplayName("출석 성공 시 isAttended=true로 저장된다")
    void execute_savesAttendanceAsTrue_whenSuccessful() {
        MarkAttendanceData.Command command = new MarkAttendanceData.Command(new Actor(USER_ID));
        UserAttendance notAttended = createAttendance(false);
        User user = createUser(USER_ID, 3);

        when(userAttendanceRepositoryPort.findByUserIdAndAttendanceDate(USER_ID, ATTENDANCE_DATE))
                .thenReturn(Optional.of(notAttended));
        when(userRepositoryPort.findByIdForUpdate(USER_ID))
                .thenReturn(Optional.of(user));
        when(userRepositoryPort.save(argThat(u -> u.currentAttendanceStreak() == 4)))
                .thenReturn(user.incrementAttendanceStreak());

        markAttendanceService.execute(command);

        verify(userAttendanceRepositoryPort).save(argThat(UserAttendance::isAttended));
    }

    @Test
    @DisplayName("출석 성공 시 streak이 1 증가한 값을 반환한다")
    void execute_returnsIncrementedStreak_whenSuccessful() {
        MarkAttendanceData.Command command = new MarkAttendanceData.Command(new Actor(USER_ID));
        User user = createUser(USER_ID, 3);

        when(userAttendanceRepositoryPort.findByUserIdAndAttendanceDate(USER_ID, ATTENDANCE_DATE))
                .thenReturn(Optional.of(createAttendance(false)));
        when(userRepositoryPort.findByIdForUpdate(USER_ID))
                .thenReturn(Optional.of(user));
        when(userRepositoryPort.save(argThat(u -> u.currentAttendanceStreak() == 4)))
                .thenReturn(user.incrementAttendanceStreak());

        MarkAttendanceData.Result result = markAttendanceService.execute(command);

        assertThat(result.attendanceStreak()).isEqualTo(4);
    }

    @Test
    @DisplayName("유저가 존재하지 않으면 UserNotFoundException을 던진다")
    void execute_throwsUserNotFoundException_whenUserNotFound() {
        MarkAttendanceData.Command command = new MarkAttendanceData.Command(new Actor(USER_ID));
        when(userAttendanceRepositoryPort.findByUserIdAndAttendanceDate(USER_ID, ATTENDANCE_DATE))
                .thenReturn(Optional.of(createAttendance(false)));
        when(userRepositoryPort.findByIdForUpdate(USER_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> markAttendanceService.execute(command))
                .isInstanceOf(UserNotFoundException.class);
    }

    private UserAttendance createAttendance(boolean isAttended) {
        return new UserAttendance(1L, Instant.now(), Instant.now(), USER_ID, ATTENDANCE_DATE, isAttended);
    }

    private User createUser(Long id, int attendanceStreak) {
        return new User(
                id, Instant.now(), Instant.now(),
                "username", "user@example.com", "name", "encoded-password",
                Role.USER, "", 0, 0, Major.NONE, UserStatus.ACTIVE,
                null, false, RankTier.NONE, ExpTier.UNRANKED,
                attendanceStreak
        );
    }
}