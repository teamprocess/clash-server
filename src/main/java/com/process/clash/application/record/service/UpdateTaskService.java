package com.process.clash.application.record.service;

import com.process.clash.application.record.data.UpdateTaskData;
import com.process.clash.application.record.exception.exception.notfound.TaskNotFoundException;
import com.process.clash.application.record.policy.TaskPolicy;
import com.process.clash.application.record.port.in.UpdateTaskUseCase;
import com.process.clash.application.record.port.out.TaskRepositoryPort;
import com.process.clash.domain.record.entity.Task;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateTaskService implements UpdateTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;
    private final TaskPolicy taskPolicy;

    @Override
    public UpdateTaskData.Result execute(UpdateTaskData.Command command) {
        Task task = taskRepositoryPort.findById(command.taskId())
            .orElseThrow(TaskNotFoundException::new);

        taskPolicy.validateOwnership(command.actor(), task);

        Task updatedTask = new Task(
            task.id(),
            command.name(),
            task.studyTime(),
            task.createdAt(),
            LocalDateTime.now(),
            task.user()
        );
        taskRepositoryPort.save(updatedTask);

        return UpdateTaskData.Result.from(updatedTask);
    }
}
