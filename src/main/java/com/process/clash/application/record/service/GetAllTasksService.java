package com.process.clash.application.record.service;

import com.process.clash.application.record.data.GetAllTasksData;
import com.process.clash.application.record.port.in.GetAllTasksUseCase;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.application.record.port.out.TaskRepositoryPort;
import com.process.clash.domain.record.entity.StudySession;
import com.process.clash.domain.record.entity.Task;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    public GetAllTasksData.Result execute(GetAllTasksData.Command command) {

        List<Task> taskList = taskRepositoryPort.findAllByUserId(command.actor().id());
        List<StudySession> sessions = studySessionRepositoryPort.findAllByUserId(command.actor().id());
        LocalDateTime now = LocalDateTime.now();
        Map<Long, Long> studyTimeByTaskId = sessions.stream()
            .collect(Collectors.groupingBy(
                session -> session.task().id(),
                Collectors.summingLong(session -> ChronoUnit.SECONDS.between(
                    session.startedAt(),
                    session.endedAt() == null ? now : session.endedAt()
                ))
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
}
