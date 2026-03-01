package com.process.clash.application.record.v2.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.util.RecordDateCalculator;
import com.process.clash.application.record.v2.data.GetTodayRecordV2Data;
import com.process.clash.application.record.v2.exception.exception.badrequest.InvalidRecordV2DailyDateRequestException;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import com.process.clash.infrastructure.config.RecordProperties;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
class GetTodayRecordV2ServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;

    private GetTodayRecordV2Service getTodayRecordV2Service;

    @BeforeEach
    void setUp() {
        getTodayRecordV2Service = new GetTodayRecordV2Service(
            userRepositoryPort,
            recordSessionV2RepositoryPort,
            new RecordProperties("UTC", 6),
            ZoneId.of("UTC")
        );
    }

    @Test
    @DisplayName("date가 주어지면 해당 날짜 전체 기준으로 V2 기록을 계산한다")
    void execute_usesRequestedDateWindow() {
        Actor actor = new Actor(1L);
        User user = createUser(1L);
        LocalDate requestedDate = RecordDateCalculator.recordDate(
            ZonedDateTime.now(ZoneId.of("UTC")),
            6
        ).minusDays(1);
        LocalDateTime dayStart = requestedDate.atTime(6, 0);
        LocalDateTime dayEnd = dayStart.plusDays(1);

        RecordSessionV2 session = new RecordSessionV2(
            100L,
            actor.id(),
            RecordSessionTypeV2.TASK,
            10L,
            "자료구조",
            11L,
            "해시테이블",
            null,
            dayStart.minusHours(2).atZone(ZoneId.of("UTC")).toInstant(),
            dayEnd.plusHours(3).atZone(ZoneId.of("UTC")).toInstant()
        );

        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.of(user));
        when(recordSessionV2RepositoryPort.findAllByUserIdAndTimeRange(actor.id(), dayStart, dayEnd))
            .thenReturn(List.of(session));

        GetTodayRecordV2Data.Result result = getTodayRecordV2Service.execute(
            new GetTodayRecordV2Data.Command(actor, requestedDate)
        );

        assertThat(result.date()).isEqualTo(requestedDate.toString());
        assertThat(result.totalStudyTime()).isEqualTo(86_400L);
        assertThat(result.studyStoppedAt()).isNull();
        assertThat(result.sessions()).hasSize(1);
        assertThat(result.sessions().get(0).startedAt()).isEqualTo(dayStart.atZone(ZoneId.of("UTC")).toInstant());
        assertThat(result.sessions().get(0).endedAt()).isEqualTo(dayEnd.atZone(ZoneId.of("UTC")).toInstant());
    }

    @Test
    @DisplayName("date가 없으면 현재 record-day 기준으로 조회한다")
    void execute_usesCurrentRecordDayWhenDateIsNull() {
        Actor actor = new Actor(1L);
        User user = createUser(1L);

        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.of(user));
        when(recordSessionV2RepositoryPort.findAllByUserIdAndTimeRange(eq(actor.id()), any(), any()))
            .thenReturn(List.of());

        GetTodayRecordV2Data.Result result = getTodayRecordV2Service.execute(
            new GetTodayRecordV2Data.Command(actor, null)
        );

        ArgumentCaptor<LocalDateTime> startCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> endCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(recordSessionV2RepositoryPort).findAllByUserIdAndTimeRange(
            eq(actor.id()),
            startCaptor.capture(),
            endCaptor.capture()
        );

        LocalDateTime start = startCaptor.getValue();
        LocalDateTime end = endCaptor.getValue();

        assertThat(end).isEqualTo(start.plusDays(1));
        assertThat(result.date()).isEqualTo(start.toLocalDate().toString());
    }

    @Test
    @DisplayName("미래 날짜 조회 요청은 예외가 발생한다")
    void execute_throwsWhenRequestedDateIsFuture() {
        Actor actor = new Actor(1L);
        User user = createUser(1L);
        LocalDate todayRecordDate = RecordDateCalculator.recordDate(ZonedDateTime.now(ZoneId.of("UTC")), 6);
        LocalDate futureDate = todayRecordDate.plusDays(1);

        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> getTodayRecordV2Service.execute(
            new GetTodayRecordV2Data.Command(actor, futureDate)
        )).isInstanceOf(InvalidRecordV2DailyDateRequestException.class);
        verify(recordSessionV2RepositoryPort, never()).findAllByUserIdAndTimeRange(any(), any(), any());
    }

    @Test
    @DisplayName("과거 날짜 조회에서 진행중인 세션은 날짜 경계 시각으로 종료 시각이 보정된다")
    void execute_capsOpenSessionEndAtDayBoundaryForPastDate() {
        Actor actor = new Actor(1L);
        User user = createUser(1L);
        LocalDate requestedDate = RecordDateCalculator.recordDate(
            ZonedDateTime.now(ZoneId.of("UTC")),
            6
        ).minusDays(1);
        LocalDateTime dayStart = requestedDate.atTime(6, 0);
        LocalDateTime dayEnd = dayStart.plusDays(1);

        RecordSessionV2 openSession = new RecordSessionV2(
            101L,
            actor.id(),
            RecordSessionTypeV2.TASK,
            10L,
            "자료구조",
            11L,
            "해시테이블",
            null,
            dayStart.minusHours(1).atZone(ZoneId.of("UTC")).toInstant(),
            null
        );

        when(userRepositoryPort.findById(actor.id())).thenReturn(Optional.of(user));
        when(recordSessionV2RepositoryPort.findAllByUserIdAndTimeRange(actor.id(), dayStart, dayEnd))
            .thenReturn(List.of(openSession));

        GetTodayRecordV2Data.Result result = getTodayRecordV2Service.execute(
            new GetTodayRecordV2Data.Command(actor, requestedDate)
        );

        assertThat(result.studyStoppedAt()).isNull();
        assertThat(result.sessions()).hasSize(1);
        assertThat(result.sessions().get(0).endedAt()).isEqualTo(dayEnd.atZone(ZoneId.of("UTC")).toInstant());
    }

    private User createUser(Long id) {
        return new User(
            id,
            Instant.now(),
            Instant.now(),
            "username",
            "user@example.com",
            "name",
            "encoded-password",
            Role.USER,
            "",
            0,
            0,
            Major.NONE,
            UserStatus.ACTIVE,
            null
        );
    }
}
