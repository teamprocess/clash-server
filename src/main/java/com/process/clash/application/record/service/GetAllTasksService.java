package com.process.clash.application.record.service;

import com.process.clash.application.record.dto.GetAllTasksData;
import com.process.clash.application.record.port.in.GetAllTasksUseCase;
import com.process.clash.application.record.port.out.TaskRepositoryPort;
import com.process.clash.domain.record.model.entity.Task;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllTasksService implements GetAllTasksUseCase {

    private final TaskRepositoryPort taskRepositoryPort;

    public GetAllTasksData.Result execute(GetAllTasksData.Command command) {

        List<Task> taskList = taskRepositoryPort.findAllByUserId(command.actor().id());

        return GetAllTasksData.Result.create(taskList);
    }
}
