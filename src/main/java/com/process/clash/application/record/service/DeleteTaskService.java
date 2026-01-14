package com.process.clash.application.record.service;

import com.process.clash.application.record.dto.DeleteTaskData;
import com.process.clash.application.record.exception.exception.notfound.TaskNotFoundException;
import com.process.clash.application.record.policy.TaskPolicy;
import com.process.clash.application.record.port.in.DeleteTaskUseCase;
import com.process.clash.application.record.port.out.TaskRepositoryPort;
import com.process.clash.domain.record.model.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteTaskService implements DeleteTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;
    private final TaskPolicy taskPolicy;

    @Override
    public void execute(DeleteTaskData.Command command) {
        Task task = taskRepositoryPort.findById(command.taskId())
            .orElseThrow(TaskNotFoundException::new);

        taskPolicy.validateOwnership(command.actor(), task);
        taskRepositoryPort.deleteById(task.id());
    }
}
