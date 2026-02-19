package com.process.clash.application.record.service;

import com.process.clash.application.record.data.UpdateTaskData;
import com.process.clash.application.record.exception.exception.notfound.TaskNotFoundException;
import com.process.clash.application.record.policy.TaskPolicy;
import com.process.clash.application.record.port.in.UpdateTaskUseCase;
import com.process.clash.application.record.port.out.RecordTaskRepositoryPort;
import com.process.clash.domain.record.entity.RecordTask;
import jakarta.transaction.Transactional;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateTaskService implements UpdateTaskUseCase {

    private final RecordTaskRepositoryPort taskRepositoryPort;
    private final TaskPolicy taskPolicy;

    @Override
    public UpdateTaskData.Result execute(UpdateTaskData.Command command) {
        RecordTask task = taskRepositoryPort.findById(command.taskId())
            .orElseThrow(TaskNotFoundException::new);

        taskPolicy.validateOwnership(command.actor(), task);

        RecordTask updatedTask = new RecordTask(
            task.id(),
            command.name(),
            task.studyTime(),
            task.createdAt(),
            Instant.now(),
            task.user()
        );
        taskRepositoryPort.save(updatedTask);

        return UpdateTaskData.Result.from(updatedTask);
    }
}
