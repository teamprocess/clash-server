package com.process.clash.application.record.service;

import com.process.clash.application.record.data.GetAllTasksData;
import com.process.clash.application.record.port.in.GetAllTasksUseCase;
import com.process.clash.application.record.port.out.RecordSessionRepositoryPort;
import com.process.clash.application.record.port.out.RecordTaskRepositoryPort;
import com.process.clash.application.record.util.RecordDateCalculator;
import com.process.clash.domain.record.entity.RecordSession;
import com.process.clash.domain.record.entity.RecordTask;
import com.process.clash.domain.record.enums.RecordType;
import com.process.clash.infrastructure.config.RecordProperties;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllTasksService implements GetAllTasksUseCase {

    private final RecordTaskRepositoryPort taskRepositoryPort;
    private final RecordSessionRepositoryPort recordSessionRepositoryPort;
    private final RecordProperties recordProperties;
    private final ZoneId recordZoneId;

    public GetAllTasksData.Result execute(GetAllTasksData.Command command) {

        List<RecordTask> taskList = taskRepositoryPort.findAllByUserId(command.actor().id());
        ZonedDateTime nowZoned = ZonedDateTime.now(recordZoneId);
        int boundaryHour = recordProperties.dayBoundaryHour();
        LocalDateTime dayStart = RecordDateCalculator.startOfRecordDay(nowZoned, boundaryHour);
        LocalDateTime dayEnd = dayStart.plusDays(1);
        LocalDateTime now = nowZoned.toLocalDateTime();
        LocalDateTime endLimit = now.isBefore(dayEnd) ? now : dayEnd;
        List<RecordSession> sessions = recordSessionRepositoryPort.findAllByUserIdAndTimeRange(
            command.actor().id(),
            dayStart,
            dayEnd
        );
        Map<Long, Long> studyTimeByTaskId = sessions.stream()
            .filter(session -> session.recordType() == RecordType.TASK && session.task() != null)
            .collect(Collectors.groupingBy(
                session -> session.task().id(),
                Collectors.summingLong(session -> sessionSecondsInWindow(session, dayStart, endLimit))
            ));

        List<RecordTask> tasksWithStudyTime = taskList.stream()
            .map(task -> new RecordTask(
                task.id(),
                task.name(),
                studyTimeByTaskId.getOrDefault(task.id(), 0L),
                task.createdAt(),
                task.updatedAt(),
                task.user()
            ))
            .toList();

        return GetAllTasksData.Result.create(tasksWithStudyTime);
    }

    private long sessionSecondsInWindow(
        RecordSession session,
        LocalDateTime dayStart,
        LocalDateTime endLimit
    ) {
        LocalDateTime sessionStart = LocalDateTime.ofInstant(session.startedAt(), recordZoneId);
        LocalDateTime effectiveStart = sessionStart.isAfter(dayStart)
            ? sessionStart
            : dayStart;
        LocalDateTime effectiveEnd = session.endedAt() == null
            ? null
            : LocalDateTime.ofInstant(session.endedAt(), recordZoneId);
        if (effectiveEnd == null || effectiveEnd.isAfter(endLimit)) {
            effectiveEnd = endLimit;
        }
        if (!effectiveEnd.isAfter(effectiveStart)) {
            return 0L;
        }
        return ChronoUnit.SECONDS.between(effectiveStart, effectiveEnd);
    }
}
