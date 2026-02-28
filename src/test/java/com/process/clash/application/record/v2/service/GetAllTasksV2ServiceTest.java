package com.process.clash.application.record.v2.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.util.RecordDateCalculator;
import com.process.clash.application.record.v2.data.GetAllTasksV2Data;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordTaskV2RepositoryPort;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
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
class GetAllTasksV2ServiceTest {

    @Mock
    private RecordTaskV2RepositoryPort recordTaskV2RepositoryPort;

    @Mock
    private RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;

    private GetAllTasksV2Service getAllTasksV2Service;

    @BeforeEach
    void setUp() {
        getAllTasksV2Service = new GetAllTasksV2Service(
            recordTaskV2RepositoryPort,
            recordSessionV2RepositoryPort,
            new RecordProperties("UTC", 6),
            ZoneId.of("UTC")
        );
    }

    @Test
    @DisplayName("task 목록을 subjectId 오름차순(null 먼저)으로 조회하고 학습시간/완료상태를 반환한다")
    void execute_returnsSortedTasksWithStudyTimeAndCompletion() {
        Actor actor = new Actor(1L);
        RecordTaskV2 noSubjectTask = new RecordTaskV2(10L, 1L, null, "무주제 작업", true, 0L, Instant.now(), Instant.now());
        RecordTaskV2 subjectTask = new RecordTaskV2(11L, 1L, 5L, "주제 작업", false, 0L, Instant.now(), Instant.now());

        ZonedDateTime nowZoned = ZonedDateTime.now(ZoneId.of("UTC"));
        LocalDateTime dayStart = RecordDateCalculator.startOfRecordDay(nowZoned, 6);
        LocalDateTime dayEnd = dayStart.plusDays(1);
        LocalDateTime endLimit = nowZoned.toLocalDateTime().isBefore(dayEnd) ? nowZoned.toLocalDateTime() : dayEnd;
        long expectedWindowSeconds = ChronoUnit.SECONDS.between(dayStart, endLimit);

        RecordSessionV2 noSubjectSession = new RecordSessionV2(
            100L,
            1L,
            RecordSessionTypeV2.TASK,
            null,
            null,
            10L,
            "무주제 작업",
            null,
            dayStart.minusHours(1).atZone(ZoneId.of("UTC")).toInstant(),
            endLimit.plusHours(1).atZone(ZoneId.of("UTC")).toInstant()
        );

        when(recordTaskV2RepositoryPort.findAllByUserIdOrderBySubjectIdDescNullsFirst(1L))
            .thenReturn(List.of(noSubjectTask, subjectTask));
        when(recordSessionV2RepositoryPort.findAllByUserIdAndTimeRange(1L, dayStart, dayEnd))
            .thenReturn(List.of(noSubjectSession));

        GetAllTasksV2Data.Result result = getAllTasksV2Service.execute(new GetAllTasksV2Data.Command(actor));

        assertThat(result.tasks()).hasSize(2);
        assertThat(result.tasks().get(0).subjectId()).isNull();
        assertThat(result.tasks().get(0).completed()).isTrue();
        assertThat(result.tasks().get(0).studyTime()).isEqualTo(expectedWindowSeconds);

        assertThat(result.tasks().get(1).subjectId()).isEqualTo(5L);
        assertThat(result.tasks().get(1).completed()).isFalse();
        assertThat(result.tasks().get(1).studyTime()).isEqualTo(0L);
    }
}
