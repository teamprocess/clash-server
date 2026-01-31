package com.process.clash.application.record.service;

import com.process.clash.application.record.data.GetAllTasksData;
import com.process.clash.application.record.port.in.GetAllTasksUseCase;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.application.record.port.out.TaskRepositoryPort;
import com.process.clash.domain.record.entity.StudySession;
import com.process.clash.domain.record.entity.Task;
import com.process.clash.infrastructure.config.RecordProperties;
import java.time.LocalDate;
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

    private final TaskRepositoryPort taskRepositoryPort;
    private final StudySessionRepositoryPort studySessionRepositoryPort;
    private final RecordProperties recordProperties;
    private final ZoneId recordZoneId;

    public GetAllTasksData.Result execute(GetAllTasksData.Command command) {

        List<Task> taskList = taskRepositoryPort.findAllByUserId(command.actor().id());
        ZonedDateTime nowZoned = ZonedDateTime.now(recordZoneId);
        int boundaryHour = recordProperties.dayBoundaryHour();
        LocalDate recordDate = nowZoned.toLocalDate();
        if (nowZoned.getHour() < boundaryHour) {
            recordDate = recordDate.minusDays(1);
        }
        LocalDateTime dayStart = recordDate.atTime(boundaryHour, 0);
        LocalDateTime dayEnd = dayStart.plusDays(1);
        LocalDateTime now = nowZoned.toLocalDateTime();
        LocalDateTime endLimit = now.isBefore(dayEnd) ? now : dayEnd;
        List<StudySession> sessions = studySessionRepositoryPort.findAllByUserIdAndTimeRange(
            command.actor().id(),
            dayStart,
            dayEnd
        );
        Map<Long, Long> studyTimeByTaskId = sessions.stream()
            .collect(Collectors.groupingBy(
                session -> session.task().id(),
                Collectors.summingLong(session -> sessionSecondsInWindow(session, dayStart, endLimit))
            ));

        List<Task> tasksWithStudyTime = taskList.stream()
            .map(task -> new Task(
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
        StudySession session,
        LocalDateTime dayStart,
        LocalDateTime endLimit
    ) {
        LocalDateTime effectiveStart = session.startedAt().isAfter(dayStart)
            ? session.startedAt()
            : dayStart;
        LocalDateTime effectiveEnd = session.endedAt() == null
            ? endLimit
            : session.endedAt();
        if (effectiveEnd.isAfter(endLimit)) {
            effectiveEnd = endLimit;
        }
        if (!effectiveEnd.isAfter(effectiveStart)) {
            return 0L;
        }
        return ChronoUnit.SECONDS.between(effectiveStart, effectiveEnd);
    }
}
