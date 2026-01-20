package com.process.clash.application.record.service;

import com.process.clash.application.record.data.DeleteTaskData;
import com.process.clash.application.record.exception.exception.conflict.TaskHasActiveSessionException;
import com.process.clash.application.record.exception.exception.notfound.TaskNotFoundException;
import com.process.clash.application.record.policy.TaskPolicy;
import com.process.clash.application.record.port.in.DeleteTaskUseCase;
import com.process.clash.application.record.port.out.StudySessionRepositoryPort;
import com.process.clash.application.record.port.out.TaskRepositoryPort;
import com.process.clash.domain.record.model.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteTaskService implements DeleteTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;
    private final TaskPolicy taskPolicy;
    private final StudySessionRepositoryPort studySessionRepositoryPort;

    @Override
    public void execute(DeleteTaskData.Command command) {
        Task task = taskRepositoryPort.findById(command.taskId())
            .orElseThrow(TaskNotFoundException::new);

        taskPolicy.validateOwnership(command.actor(), task);
        if (studySessionRepositoryPort.existsActiveSessionByTaskId(task.id())) {
            throw new TaskHasActiveSessionException();
        }
        taskRepositoryPort.deleteById(task.id());
    }
}
