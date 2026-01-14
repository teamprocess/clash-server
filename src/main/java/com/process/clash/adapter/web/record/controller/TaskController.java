package com.process.clash.adapter.web.record.controller;

import com.process.clash.adapter.web.record.dto.GetAllTasksDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.dto.GetAllTasksData;
import com.process.clash.application.record.port.in.GetAllTasksUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/record/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final GetAllTasksUseCase getAllTasksUseCase;

    @GetMapping
    public GetAllTasksDto.Response getAllTasks(
        @AuthenticatedActor Actor actor
    ) {

        GetAllTasksData.Command command = new GetAllTasksData.Command(actor);
        GetAllTasksData.Result result = getAllTasksUseCase.execute(command);

        return GetAllTasksDto.Response.from(result);
    }
}
