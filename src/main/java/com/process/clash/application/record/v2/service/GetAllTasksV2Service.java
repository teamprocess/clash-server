package com.process.clash.application.record.v2.service;

import com.process.clash.application.record.util.RecordDayWindow;
import com.process.clash.application.record.util.RecordSessionWindowCalculator;
import com.process.clash.application.record.v2.data.GetAllTasksV2Data;
import com.process.clash.application.record.v2.port.in.GetAllTasksV2UseCase;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordTaskV2RepositoryPort;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.entity.RecordTaskV2;
import com.process.clash.infrastructure.config.RecordProperties;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllTasksV2Service implements GetAllTasksV2UseCase {

    private final RecordTaskV2RepositoryPort recordTaskV2RepositoryPort;
    private final RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;
    private final RecordProperties recordProperties;
    private final ZoneId recordZoneId;

    @Override
    public GetAllTasksV2Data.Result execute(GetAllTasksV2Data.Command command) {
        List<RecordTaskV2> tasks = recordTaskV2RepositoryPort.findAllByUserIdOrderBySubjectIdDescNullsFirst(
            command.actor().id()
        );
        if (tasks.isEmpty()) {
            return GetAllTasksV2Data.Result.from(List.of());
        }

        RecordDayWindow dayWindow = RecordDayWindow.today(recordZoneId, recordProperties.dayBoundaryHour());
        LocalDateTime dayStart = dayWindow.dayStart();
        LocalDateTime dayEnd = dayWindow.dayEnd();
        LocalDateTime endLimit = dayWindow.endLimit();

        List<RecordSessionV2> sessions = recordSessionV2RepositoryPort.findAllByUserIdAndTimeRange(
            command.actor().id(),
            dayStart,
            dayEnd
        );

        Map<Long, Long> taskStudyTimes = RecordSessionWindowCalculator.taskStudyTimesInWindow(
            sessions,
            dayStart,
            endLimit,
            recordZoneId
        );

        List<GetAllTasksV2Data.TaskSummary> summaries = tasks.stream()
            .map(task -> new GetAllTasksV2Data.TaskSummary(
                task.id(),
                task.subjectId(),
                task.name(),
                task.completed(),
                taskStudyTimes.getOrDefault(task.id(), 0L)
            ))
            .toList();

        return GetAllTasksV2Data.Result.from(summaries);
    }
}
