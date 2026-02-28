package com.process.clash.application.record.v2.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.v2.data.GetActivityStatisticsV2Data;
import com.process.clash.application.record.v2.exception.exception.badrequest.InvalidActivityStatisticsDurationException;
import com.process.clash.application.record.v2.port.out.RecordDevelopSessionSegmentV2RepositoryPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.common.enums.PeriodCategory;
import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import com.process.clash.infrastructure.config.RecordProperties;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetActivityStatisticsV2ServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private RecordDevelopSessionSegmentV2RepositoryPort recordDevelopSessionSegmentV2RepositoryPort;

    private GetActivityStatisticsV2Service getActivityStatisticsV2Service;

    @BeforeEach
    void setUp() {
        getActivityStatisticsV2Service = new GetActivityStatisticsV2Service(
            userRepositoryPort,
            recordDevelopSessionSegmentV2RepositoryPort,
            new RecordProperties("UTC", 6),
            java.time.ZoneId.of("UTC")
        );
    }

    @Test
    @DisplayName("WEEK 조회는 앱별 총 활동 시간을 7일 평균으로 반환한다")
    void execute_returnsWeeklyAverageStudyTimeByApp() {
        Actor actor = new Actor(1L);
        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.of(createUser(actor.id())));
        when(recordDevelopSessionSegmentV2RepositoryPort.findAppActivityTotalsByUserIdAndRange(
            eq(actor.id()), any(), any(), any()
        )).thenReturn(List.of(
            new RecordDevelopSessionSegmentV2RepositoryPort.AppActivityTotal(MonitoredApp.VSCODE, 7000L),
            new RecordDevelopSessionSegmentV2RepositoryPort.AppActivityTotal(MonitoredApp.INTELLIJ_IDEA, 1400L)
        ));

        GetActivityStatisticsV2Data.Result result = getActivityStatisticsV2Service.execute(
            new GetActivityStatisticsV2Data.Command(actor, PeriodCategory.WEEK)
        );

        assertThat(result.apps()).containsExactly(
            new GetActivityStatisticsV2Data.AppActivity("VSCODE", 1000),
            new GetActivityStatisticsV2Data.AppActivity("INTELLIJ_IDEA", 200)
        );

        ArgumentCaptor<Instant> startCaptor = ArgumentCaptor.forClass(Instant.class);
        ArgumentCaptor<Instant> endCaptor = ArgumentCaptor.forClass(Instant.class);
        ArgumentCaptor<Instant> nowCaptor = ArgumentCaptor.forClass(Instant.class);

        verify(recordDevelopSessionSegmentV2RepositoryPort).findAppActivityTotalsByUserIdAndRange(
            eq(actor.id()),
            startCaptor.capture(),
            endCaptor.capture(),
            nowCaptor.capture()
        );

        Instant start = startCaptor.getValue();
        Instant end = endCaptor.getValue();
        Instant now = nowCaptor.getValue();

        assertThat(now).isEqualTo(end);
        assertThat(start).isBefore(end);
        assertThat(Duration.between(start, end))
            .isGreaterThanOrEqualTo(Duration.ofDays(6))
            .isLessThan(Duration.ofDays(7));
    }

    @Test
    @DisplayName("MONTH 조회는 앱별 총 활동 시간을 30일 평균으로 반환한다")
    void execute_returnsMonthlyAverageStudyTimeByApp() {
        Actor actor = new Actor(2L);
        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.of(createUser(actor.id())));
        when(recordDevelopSessionSegmentV2RepositoryPort.findAppActivityTotalsByUserIdAndRange(
            eq(actor.id()), any(), any(), any()
        )).thenReturn(List.of(
            new RecordDevelopSessionSegmentV2RepositoryPort.AppActivityTotal(MonitoredApp.WEBSTORM, 9_000L)
        ));

        GetActivityStatisticsV2Data.Result result = getActivityStatisticsV2Service.execute(
            new GetActivityStatisticsV2Data.Command(actor, PeriodCategory.MONTH)
        );

        assertThat(result.apps()).containsExactly(
            new GetActivityStatisticsV2Data.AppActivity("WEBSTORM", 300)
        );
    }

    @Test
    @DisplayName("지원하지 않는 duration이면 예외를 던진다")
    void execute_throwsWhenDurationIsUnsupported() {
        Actor actor = new Actor(3L);
        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.of(createUser(actor.id())));

        assertThatThrownBy(() -> getActivityStatisticsV2Service.execute(
            new GetActivityStatisticsV2Data.Command(actor, PeriodCategory.YEAR)
        )).isInstanceOf(InvalidActivityStatisticsDurationException.class);

        verify(recordDevelopSessionSegmentV2RepositoryPort, never())
            .findAppActivityTotalsByUserIdAndRange(any(), any(), any(), any());
    }

    private User createUser(Long id) {
        return new User(
            id,
            Instant.now(),
            Instant.now(),
            "username-" + id,
            "user" + id + "@example.com",
            "name",
            "encoded-password",
            Role.USER,
            "",
            0,
            0,
            Major.NONE,
            UserStatus.ACTIVE
        );
    }
}
