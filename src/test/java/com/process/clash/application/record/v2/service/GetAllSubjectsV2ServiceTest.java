package com.process.clash.application.record.v2.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.util.RecordDateCalculator;
import com.process.clash.application.record.v2.data.GetAllSubjectsV2Data;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordSubjectV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordTaskV2RepositoryPort;
import com.process.clash.domain.record.enums.MonitoredApp;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.entity.RecordSubjectV2;
import com.process.clash.domain.record.v2.entity.RecordTaskV2;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import com.process.clash.infrastructure.config.RecordProperties;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetAllSubjectsV2ServiceTest {

    @Mock
    private RecordSubjectV2RepositoryPort recordSubjectV2RepositoryPort;

    @Mock
    private RecordTaskV2RepositoryPort recordTaskV2RepositoryPort;

    @Mock
    private RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;

    private GetAllSubjectsV2Service getAllSubjectsV2Service;

    @BeforeEach
    void setUp() {
        getAllSubjectsV2Service = new GetAllSubjectsV2Service(
            recordSubjectV2RepositoryPort,
            recordTaskV2RepositoryPort,
            recordSessionV2RepositoryPort,
            new RecordProperties("UTC", 6),
            ZoneId.of("UTC")
        );
    }

    @Test
    @DisplayName("과목 그룹이 없으면 빈 목록을 반환한다")
    void execute_returnsEmptyWhenNoSubjects() {
        Actor actor = new Actor(1L);
        when(recordSubjectV2RepositoryPort.findAllByUserId(actor.id())).thenReturn(List.of());

        GetAllSubjectsV2Data.Result result = getAllSubjectsV2Service.execute(new GetAllSubjectsV2Data.Command(actor));

        assertThat(result.subjects()).isEmpty();
    }

    @Test
    @DisplayName("과목 그룹/세부 작업별 오늘 학습 시간을 집계한다")
    void execute_aggregatesStudyTimeBySubjectAndTask() {
        Actor actor = new Actor(1L);
        RecordSubjectV2 subject1 = new RecordSubjectV2(10L, 1L, "백엔드", 0L, Instant.now(), Instant.now());
        RecordSubjectV2 subject2 = new RecordSubjectV2(20L, 1L, "알고리즘", 0L, Instant.now(), Instant.now());
        RecordTaskV2 task1 = new RecordTaskV2(11L, 1L, 10L, "ERD", false, 0L, Instant.now(), Instant.now());
        RecordTaskV2 task2 = new RecordTaskV2(12L, 1L, 10L, "API", true, 0L, Instant.now(), Instant.now());
        RecordTaskV2 task3 = new RecordTaskV2(21L, 1L, 20L, "DP", false, 0L, Instant.now(), Instant.now());

        ZonedDateTime nowZoned = ZonedDateTime.now(ZoneId.of("UTC"));
        LocalDateTime dayStart = RecordDateCalculator.startOfRecordDay(nowZoned, 6);
        LocalDateTime dayEnd = dayStart.plusDays(1);
        LocalDateTime endLimit = nowZoned.toLocalDateTime().isBefore(dayEnd) ? nowZoned.toLocalDateTime() : dayEnd;
        long expectedWindowSeconds = ChronoUnit.SECONDS.between(dayStart, endLimit);

        RecordSessionV2 subject1TaskSession = new RecordSessionV2(
            100L,
            1L,
            RecordSessionTypeV2.TASK,
            10L,
            "백엔드",
            11L,
            "ERD",
            null,
            dayStart.minusHours(1).atZone(ZoneId.of("UTC")).toInstant(),
            endLimit.plusHours(1).atZone(ZoneId.of("UTC")).toInstant()
        );
        RecordSessionV2 subject1OnlySession = new RecordSessionV2(
            101L,
            1L,
            RecordSessionTypeV2.TASK,
            10L,
            "백엔드",
            null,
            null,
            null,
            dayStart.minusHours(2).atZone(ZoneId.of("UTC")).toInstant(),
            endLimit.plusHours(2).atZone(ZoneId.of("UTC")).toInstant()
        );
        RecordSessionV2 developSession = new RecordSessionV2(
            102L,
            1L,
            RecordSessionTypeV2.DEVELOP,
            null,
            null,
            null,
            null,
            MonitoredApp.VSCODE,
            dayStart.minusHours(3).atZone(ZoneId.of("UTC")).toInstant(),
            endLimit.plusHours(3).atZone(ZoneId.of("UTC")).toInstant()
        );

        when(recordSubjectV2RepositoryPort.findAllByUserId(actor.id())).thenReturn(List.of(subject1, subject2));
        when(recordTaskV2RepositoryPort.findAllBySubjectIds(List.of(10L, 20L))).thenReturn(List.of(task1, task2, task3));
        when(recordSessionV2RepositoryPort.findAllByUserIdAndTimeRange(actor.id(), dayStart, dayEnd))
            .thenReturn(List.of(subject1TaskSession, subject1OnlySession, developSession));

        GetAllSubjectsV2Data.Result result = getAllSubjectsV2Service.execute(new GetAllSubjectsV2Data.Command(actor));

        assertThat(result.subjects()).hasSize(2);

        GetAllSubjectsV2Data.SubjectSummary backend = result.subjects().stream()
            .filter(subject -> subject.id().equals(10L))
            .findFirst()
            .orElseThrow();
        GetAllSubjectsV2Data.SubjectSummary algorithm = result.subjects().stream()
            .filter(subject -> subject.id().equals(20L))
            .findFirst()
            .orElseThrow();

        assertThat(backend.studyTime()).isEqualTo(expectedWindowSeconds * 2);
        assertThat(algorithm.studyTime()).isEqualTo(0L);

        GetAllSubjectsV2Data.TaskSummary erd = backend.tasks().stream()
            .filter(task -> task.id().equals(11L))
            .findFirst()
            .orElseThrow();
        GetAllSubjectsV2Data.TaskSummary api = backend.tasks().stream()
            .filter(task -> task.id().equals(12L))
            .findFirst()
            .orElseThrow();
        GetAllSubjectsV2Data.TaskSummary dp = algorithm.tasks().stream()
            .filter(task -> task.id().equals(21L))
            .findFirst()
            .orElseThrow();

        assertThat(erd.studyTime()).isEqualTo(expectedWindowSeconds);
        assertThat(erd.completed()).isFalse();
        assertThat(api.studyTime()).isEqualTo(0L);
        assertThat(api.completed()).isTrue();
        assertThat(dp.studyTime()).isEqualTo(0L);
        assertThat(dp.completed()).isFalse();
    }
}
