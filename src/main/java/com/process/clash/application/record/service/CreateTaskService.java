package com.process.clash.application.record.service;

import com.process.clash.application.record.dto.CreateTaskData;
import com.process.clash.application.record.port.in.CreateTaskUseCase;
import com.process.clash.application.record.port.out.TaskRepositoryPort;
import com.process.clash.application.user.port.out.UserRepositoryPort;
import com.process.clash.application.mainpage.exception.exception.notfound.UserNotFoundException;
import com.process.clash.domain.record.model.entity.Task;
import com.process.clash.domain.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTaskService implements CreateTaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public void execute(CreateTaskData.Command command) {
        User user = userRepositoryPort.findById(command.actor().userId())
            .orElseThrow(UserNotFoundException::new);

        Task task = Task.create(command.name(), command.color(), user);
        taskRepositoryPort.save(task);
    }
}
