package com.process.clash.application.record.v2.service;

import com.process.clash.application.record.util.RecordDayWindow;
import com.process.clash.application.record.util.RecordSessionWindowCalculator;
import com.process.clash.application.record.v2.data.GetAllSubjectsV2Data;
import com.process.clash.application.record.v2.port.in.GetAllSubjectsV2UseCase;
import com.process.clash.application.record.v2.port.out.RecordSessionV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordSubjectV2RepositoryPort;
import com.process.clash.application.record.v2.port.out.RecordTaskV2RepositoryPort;
import com.process.clash.domain.record.v2.entity.RecordSessionV2;
import com.process.clash.domain.record.v2.entity.RecordSubjectV2;
import com.process.clash.domain.record.v2.entity.RecordTaskV2;
import com.process.clash.domain.record.v2.enums.RecordSessionTypeV2;
import com.process.clash.infrastructure.config.RecordProperties;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllSubjectsV2Service implements GetAllSubjectsV2UseCase {

    private final RecordSubjectV2RepositoryPort recordSubjectV2RepositoryPort;
    private final RecordTaskV2RepositoryPort recordTaskV2RepositoryPort;
    private final RecordSessionV2RepositoryPort recordSessionV2RepositoryPort;
    private final RecordProperties recordProperties;
    private final ZoneId recordZoneId;

    @Override
    public GetAllSubjectsV2Data.Result execute(GetAllSubjectsV2Data.Command command) {
        List<RecordSubjectV2> subjects = recordSubjectV2RepositoryPort.findAllByUserId(command.actor().id());
        if (subjects.isEmpty()) {
            return GetAllSubjectsV2Data.Result.from(List.of());
        }

        List<Long> subjectIds = subjects.stream().map(RecordSubjectV2::id).toList();
        List<RecordTaskV2> tasks = recordTaskV2RepositoryPort.findAllBySubjectIds(subjectIds);

        RecordDayWindow dayWindow = RecordDayWindow.today(recordZoneId, recordProperties.dayBoundaryHour());
        LocalDateTime dayStart = dayWindow.dayStart();
        LocalDateTime dayEnd = dayWindow.dayEnd();
        LocalDateTime endLimit = dayWindow.endLimit();

        List<RecordSessionV2> sessions = recordSessionV2RepositoryPort.findAllByUserIdAndTimeRange(
            command.actor().id(),
            dayStart,
            dayEnd
        );

        // 과목/세부작업 학습시간은 TASK 세션만 집계
        Set<Long> subjectIdSet = Set.copyOf(subjectIds);
        Map<Long, Long> subjectStudyTimes = sessions.stream()
            .filter(session -> session.sessionType() == RecordSessionTypeV2.TASK)
            .filter(session -> session.subjectId() != null && subjectIdSet.contains(session.subjectId()))
            .collect(Collectors.groupingBy(
                RecordSessionV2::subjectId,
                Collectors.summingLong(session -> RecordSessionWindowCalculator.secondsInWindow(
                    session,
                    dayStart,
                    endLimit,
                    recordZoneId
                ))
            ));

        Map<Long, Long> taskStudyTimes = sessions.stream()
            .filter(session -> session.sessionType() == RecordSessionTypeV2.TASK)
            .filter(session -> session.taskId() != null)
            .collect(Collectors.groupingBy(
                RecordSessionV2::taskId,
                Collectors.summingLong(session -> RecordSessionWindowCalculator.secondsInWindow(
                    session,
                    dayStart,
                    endLimit,
                    recordZoneId
                ))
            ));

        Map<Long, List<RecordTaskV2>> tasksBySubjectId = tasks.stream()
            .collect(Collectors.groupingBy(RecordTaskV2::subjectId));

        // 과목별 요약 + 하위 task 요약을 한 번에 구성
        List<GetAllSubjectsV2Data.SubjectSummary> summaries = subjects.stream()
            .map(subject -> {
                List<RecordTaskV2> subjectTasks = tasksBySubjectId.getOrDefault(subject.id(), Collections.emptyList());
                List<GetAllSubjectsV2Data.TaskSummary> taskSummaries = subjectTasks.stream()
                    .map(task -> new GetAllSubjectsV2Data.TaskSummary(
                        task.id(),
                        task.name(),
                        taskStudyTimes.getOrDefault(task.id(), 0L)
                    ))
                    .toList();

                return new GetAllSubjectsV2Data.SubjectSummary(
                    subject.id(),
                    subject.name(),
                    subjectStudyTimes.getOrDefault(subject.id(), 0L),
                    taskSummaries
                );
            })
            .toList();

        return GetAllSubjectsV2Data.Result.from(summaries);
    }
}
