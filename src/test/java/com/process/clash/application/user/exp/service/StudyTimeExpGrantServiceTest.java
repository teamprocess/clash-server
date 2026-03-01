package com.process.clash.application.user.exp.service;

import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.application.user.userexphistory.port.out.UserExpHistoryRepositoryPort;
import com.process.clash.application.user.userstudytime.port.out.UserStudyTimeRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import com.process.clash.domain.user.userexphistory.entity.UserExpHistory;
import com.process.clash.domain.user.userexphistory.enums.ExpActingCategory;
import com.process.clash.domain.user.userstudytime.entity.UserStudyTime;
import com.process.clash.infrastructure.config.RecordProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudyTimeExpGrantServiceTest {

    @Mock
    private UserStudyTimeRepositoryPort userStudyTimeRepositoryPort;

    @Mock
    private UserExpHistoryRepositoryPort userExpHistoryRepositoryPort;

    @Mock
    private UserRepositoryPort userRepositoryPort;

    private StudyTimeExpGrantService studyTimeExpGrantService;

    private static final ZoneId ZONE = ZoneId.of("Asia/Seoul");
    private static final int BOUNDARY_HOUR = 6;
    private static final Long USER_ID = 1L;

    @BeforeEach
    void setUp() {
        studyTimeExpGrantService = new StudyTimeExpGrantService(
            userStudyTimeRepositoryPort,
            userExpHistoryRepositoryPort,
            userRepositoryPort,
            new RecordProperties("Asia/Seoul", BOUNDARY_HOUR),
            ZONE
        );
    }

    @Test
    @DisplayName("최초 세션 종료 시 user_study_times 레코드를 신규 생성한다")
    void grant_createsNewStudyTimeRecord() {
        Instant startedAt = Instant.parse("2025-01-15T01:00:00Z"); // Seoul 10:00 AM
        Instant endedAt = startedAt.plusSeconds(3600); // 1시간
        LocalDate expectedDate = LocalDate.of(2025, 1, 15);

        when(userStudyTimeRepositoryPort.findByUserIdAndDate(USER_ID, expectedDate)).thenReturn(Optional.empty());
        when(userStudyTimeRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(userExpHistoryRepositoryPort.findByUserIdAndDateAndCategory(USER_ID, expectedDate, ExpActingCategory.STUDY_TIME))
            .thenReturn(Optional.empty());
        when(userExpHistoryRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(userRepositoryPort.findByIdForUpdate(USER_ID)).thenReturn(Optional.of(createUser(USER_ID, 0)));
        when(userRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        studyTimeExpGrantService.grant(USER_ID, startedAt, endedAt);

        ArgumentCaptor<UserStudyTime> studyTimeCaptor = ArgumentCaptor.forClass(UserStudyTime.class);
        verify(userStudyTimeRepositoryPort).save(studyTimeCaptor.capture());
        assertThat(studyTimeCaptor.getValue().id()).isNull();
        assertThat(studyTimeCaptor.getValue().totalStudyTimeSeconds()).isEqualTo(3600L);
        assertThat(studyTimeCaptor.getValue().date()).isEqualTo(expectedDate);
    }

    @Test
    @DisplayName("같은 날 두 번째 세션 종료 시 기존 학습시간에 누적된다")
    void grant_accumulatesStudyTimeForSameDay() {
        Instant startedAt = Instant.parse("2025-01-15T03:00:00Z"); // Seoul 12:00 PM
        Instant endedAt = startedAt.plusSeconds(1800); // 30분
        LocalDate expectedDate = LocalDate.of(2025, 1, 15);

        UserStudyTime existing = new UserStudyTime(10L, expectedDate, 3600L, USER_ID); // 기존 1시간

        when(userStudyTimeRepositoryPort.findByUserIdAndDate(USER_ID, expectedDate)).thenReturn(Optional.of(existing));
        when(userStudyTimeRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(userExpHistoryRepositoryPort.findByUserIdAndDateAndCategory(USER_ID, expectedDate, ExpActingCategory.STUDY_TIME))
            .thenReturn(Optional.empty());
        when(userExpHistoryRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(userRepositoryPort.findByIdForUpdate(USER_ID)).thenReturn(Optional.of(createUser(USER_ID, 0)));
        when(userRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        studyTimeExpGrantService.grant(USER_ID, startedAt, endedAt);

        ArgumentCaptor<UserStudyTime> studyTimeCaptor = ArgumentCaptor.forClass(UserStudyTime.class);
        verify(userStudyTimeRepositoryPort).save(studyTimeCaptor.capture());
        assertThat(studyTimeCaptor.getValue().id()).isEqualTo(10L); // 기존 ID 유지
        assertThat(studyTimeCaptor.getValue().totalStudyTimeSeconds()).isEqualTo(3600L + 1800L);
    }

    @Test
    @DisplayName("일일 최대 36000초(600분)를 초과하면 36000초로 제한된다")
    void grant_capsStudyTimeAt36000Seconds() {
        Instant startedAt = Instant.parse("2025-01-15T01:00:00Z");
        Instant endedAt = startedAt.plusSeconds(40000); // 36000초를 초과
        LocalDate expectedDate = LocalDate.of(2025, 1, 15);

        when(userStudyTimeRepositoryPort.findByUserIdAndDate(USER_ID, expectedDate)).thenReturn(Optional.empty());
        when(userStudyTimeRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(userExpHistoryRepositoryPort.findByUserIdAndDateAndCategory(USER_ID, expectedDate, ExpActingCategory.STUDY_TIME))
            .thenReturn(Optional.empty());
        when(userExpHistoryRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(userRepositoryPort.findByIdForUpdate(USER_ID)).thenReturn(Optional.of(createUser(USER_ID, 0)));
        when(userRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        studyTimeExpGrantService.grant(USER_ID, startedAt, endedAt);

        ArgumentCaptor<UserStudyTime> studyTimeCaptor = ArgumentCaptor.forClass(UserStudyTime.class);
        verify(userStudyTimeRepositoryPort).save(studyTimeCaptor.capture());
        assertThat(studyTimeCaptor.getValue().totalStudyTimeSeconds()).isEqualTo(36000L);
    }

    @Test
    @DisplayName("EXP는 분당 10점으로 계산된다 (60분 = 600점)")
    void grant_calculatesExpAt10PointsPerMinute() {
        Instant startedAt = Instant.parse("2025-01-15T01:00:00Z");
        Instant endedAt = startedAt.plusSeconds(3600); // 60분
        LocalDate expectedDate = LocalDate.of(2025, 1, 15);
        int expectedExp = 60 * 10; // 600

        when(userStudyTimeRepositoryPort.findByUserIdAndDate(USER_ID, expectedDate)).thenReturn(Optional.empty());
        when(userStudyTimeRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(userExpHistoryRepositoryPort.findByUserIdAndDateAndCategory(USER_ID, expectedDate, ExpActingCategory.STUDY_TIME))
            .thenReturn(Optional.empty());
        when(userExpHistoryRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(userRepositoryPort.findByIdForUpdate(USER_ID)).thenReturn(Optional.of(createUser(USER_ID, 0)));
        when(userRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        studyTimeExpGrantService.grant(USER_ID, startedAt, endedAt);

        ArgumentCaptor<UserExpHistory> historyCaptor = ArgumentCaptor.forClass(UserExpHistory.class);
        verify(userExpHistoryRepositoryPort).save(historyCaptor.capture());
        assertThat(historyCaptor.getValue().earnExp()).isEqualTo(expectedExp);
        assertThat(historyCaptor.getValue().actingCategory()).isEqualTo(ExpActingCategory.STUDY_TIME);
    }

    @Test
    @DisplayName("학습시간이 증가하면 delta만큼 user.totalExp가 증가한다")
    void grant_increasesTotalExpByDelta() {
        Instant startedAt = Instant.parse("2025-01-15T01:00:00Z");
        Instant endedAt = startedAt.plusSeconds(1800); // 30분
        LocalDate expectedDate = LocalDate.of(2025, 1, 15);

        UserStudyTime existingStudyTime = new UserStudyTime(10L, expectedDate, 1800L, USER_ID); // 기존 30분
        UserExpHistory existingExp = new UserExpHistory(
            99L, Instant.now(), expectedDate, 300, ExpActingCategory.STUDY_TIME, USER_ID // 기존 30분 * 10 = 300
        );
        User user = createUser(USER_ID, 300);

        when(userStudyTimeRepositoryPort.findByUserIdAndDate(USER_ID, expectedDate)).thenReturn(Optional.of(existingStudyTime));
        when(userStudyTimeRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(userExpHistoryRepositoryPort.findByUserIdAndDateAndCategory(USER_ID, expectedDate, ExpActingCategory.STUDY_TIME))
            .thenReturn(Optional.of(existingExp));
        when(userExpHistoryRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(userRepositoryPort.findByIdForUpdate(USER_ID)).thenReturn(Optional.of(user));
        when(userRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        studyTimeExpGrantService.grant(USER_ID, startedAt, endedAt);

        // 새 총 학습시간 = 1800 + 1800 = 3600초 = 60분 → EXP = 600
        // delta = 600 - 300 = 300
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepositoryPort).save(userCaptor.capture());
        assertThat(userCaptor.getValue().totalExp()).isEqualTo(300 + 300);
    }

    @Test
    @DisplayName("이미 최대 학습시간(36000초)에 도달했으면 EXP와 user.totalExp를 변경하지 않는다")
    void grant_doesNothingWhenAlreadyAtMaxStudyTime() {
        Instant startedAt = Instant.parse("2025-01-15T01:00:00Z");
        Instant endedAt = startedAt.plusSeconds(3600);
        LocalDate expectedDate = LocalDate.of(2025, 1, 15);

        UserStudyTime existingStudyTime = new UserStudyTime(10L, expectedDate, 36000L, USER_ID); // 이미 최대
        UserExpHistory existingExp = new UserExpHistory(
            99L, Instant.now(), expectedDate, 6000, ExpActingCategory.STUDY_TIME, USER_ID // 600분 * 10 = 6000
        );

        when(userStudyTimeRepositoryPort.findByUserIdAndDate(USER_ID, expectedDate)).thenReturn(Optional.of(existingStudyTime));
        when(userStudyTimeRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(userExpHistoryRepositoryPort.findByUserIdAndDateAndCategory(USER_ID, expectedDate, ExpActingCategory.STUDY_TIME))
            .thenReturn(Optional.of(existingExp));

        studyTimeExpGrantService.grant(USER_ID, startedAt, endedAt);

        verify(userExpHistoryRepositoryPort, never()).save(any());
        verify(userRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("duration이 0 이하면 아무것도 처리하지 않는다")
    void grant_doesNothingWhenDurationIsZeroOrNegative() {
        Instant now = Instant.now();

        studyTimeExpGrantService.grant(USER_ID, now, now); // duration = 0

        verifyNoInteractions(userStudyTimeRepositoryPort);
        verifyNoInteractions(userExpHistoryRepositoryPort);
        verifyNoInteractions(userRepositoryPort);
    }

    @Test
    @DisplayName("오전 6시 이전에 시작된 세션은 전날 학습일로 처리된다")
    void grant_usesYesterdayAsStudyDateWhenStartedBeforeBoundary() {
        // Seoul 기준 오전 3시 = UTC 전날 18시
        Instant startedAt = Instant.parse("2025-01-14T18:00:00Z"); // Seoul 1월 15일 03:00 → study date 1월 14일
        Instant endedAt = startedAt.plusSeconds(1800);
        LocalDate expectedDate = LocalDate.of(2025, 1, 14); // 전날

        when(userStudyTimeRepositoryPort.findByUserIdAndDate(USER_ID, expectedDate)).thenReturn(Optional.empty());
        when(userStudyTimeRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(userExpHistoryRepositoryPort.findByUserIdAndDateAndCategory(USER_ID, expectedDate, ExpActingCategory.STUDY_TIME))
            .thenReturn(Optional.empty());
        when(userExpHistoryRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(userRepositoryPort.findByIdForUpdate(USER_ID)).thenReturn(Optional.of(createUser(USER_ID, 0)));
        when(userRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        studyTimeExpGrantService.grant(USER_ID, startedAt, endedAt);

        ArgumentCaptor<UserStudyTime> studyTimeCaptor = ArgumentCaptor.forClass(UserStudyTime.class);
        verify(userStudyTimeRepositoryPort).save(studyTimeCaptor.capture());
        assertThat(studyTimeCaptor.getValue().date()).isEqualTo(expectedDate);
    }

    @Test
    @DisplayName("오전 6시 이후에 시작된 세션은 당일 학습일로 처리된다")
    void grant_usesTodayAsStudyDateWhenStartedAfterBoundary() {
        // Seoul 기준 오전 10시 = UTC 01시
        Instant startedAt = Instant.parse("2025-01-15T01:00:00Z"); // Seoul 1월 15일 10:00 → study date 1월 15일
        Instant endedAt = startedAt.plusSeconds(1800);
        LocalDate expectedDate = LocalDate.of(2025, 1, 15);

        when(userStudyTimeRepositoryPort.findByUserIdAndDate(USER_ID, expectedDate)).thenReturn(Optional.empty());
        when(userStudyTimeRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(userExpHistoryRepositoryPort.findByUserIdAndDateAndCategory(USER_ID, expectedDate, ExpActingCategory.STUDY_TIME))
            .thenReturn(Optional.empty());
        when(userExpHistoryRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(userRepositoryPort.findByIdForUpdate(USER_ID)).thenReturn(Optional.of(createUser(USER_ID, 0)));
        when(userRepositoryPort.save(any())).thenAnswer(inv -> inv.getArgument(0));

        studyTimeExpGrantService.grant(USER_ID, startedAt, endedAt);

        ArgumentCaptor<UserStudyTime> studyTimeCaptor = ArgumentCaptor.forClass(UserStudyTime.class);
        verify(userStudyTimeRepositoryPort).save(studyTimeCaptor.capture());
        assertThat(studyTimeCaptor.getValue().date()).isEqualTo(expectedDate);
    }

    // --- helper ---

    private User createUser(Long id, int totalExp) {
        return new User(id, Instant.now(), Instant.now(), "user" + id, "user@example.com",
            "name", "pw", Role.USER, "", totalExp, 0, Major.NONE, UserStatus.ACTIVE, null);
    }
}
