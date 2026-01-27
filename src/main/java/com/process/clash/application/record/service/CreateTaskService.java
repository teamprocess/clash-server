package com.process.clash.application.record.service;

import com.process.clash.application.record.data.CreateTaskData;
import com.process.clash.application.record.port.in.CreateTaskUseCase;
import com.process.clash.application.record.port.out.TaskRepositoryPort;
import com.process.clash.application.user.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.user.port.out.UserRepositoryPort;
import com.process.clash.domain.record.entity.Task;
import com.process.clash.domain.user.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateTaskService implements CreateTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public void execute(CreateTaskData.Command command) {
        User user = userRepositoryPort.findById(command.actor().id())
            .orElseThrow(UserNotFoundException::new);

        Task task = Task.create(command.name(), user);
        taskRepositoryPort.save(task);
    }
}
