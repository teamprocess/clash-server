package com.process.clash.adapter.web.record.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.record.dto.CreateTaskDto;
import com.process.clash.adapter.web.record.dto.GetAllTasksDto;
import com.process.clash.adapter.web.record.dto.UpdateTaskDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.dto.CreateTaskData;
import com.process.clash.application.record.dto.DeleteTaskData;
import com.process.clash.application.record.dto.GetAllTasksData;
import com.process.clash.application.record.dto.UpdateTaskData;
import com.process.clash.application.record.port.in.CreateTaskUseCase;
import com.process.clash.application.record.port.in.DeleteTaskUseCase;
import com.process.clash.application.record.port.in.GetAllTasksUseCase;
import com.process.clash.application.record.port.in.UpdateTaskUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/record/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final GetAllTasksUseCase getAllTasksUseCase;
    private final CreateTaskUseCase createTaskUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;

    @GetMapping
    public ApiResponse<GetAllTasksDto.Response> getAllTasks(
        @AuthenticatedActor Actor actor
    ) {

        GetAllTasksData.Command command = new GetAllTasksData.Command(actor);
        GetAllTasksData.Result result = getAllTasksUseCase.execute(command);

        return ApiResponse.success(
            GetAllTasksDto.Response.from(result),
            "과목 목록을 조회했습니다."
        );
    }

    @PostMapping
    public ApiResponse<Void> createTask(
        @AuthenticatedActor Actor actor,
        @RequestBody CreateTaskDto.Request request
    ) {
        CreateTaskData.Command command = new CreateTaskData.Command(
            actor,
            request.name(),
            request.color()
        );
        createTaskUseCase.execute(command);

        return ApiResponse.success("새로운 과목을 생성했습니다.");
    }

    @PatchMapping("/{taskId}")
    public ApiResponse<UpdateTaskDto.Response> updateTask(
        @AuthenticatedActor Actor actor,
        @PathVariable Long taskId,
        @RequestBody UpdateTaskDto.Request request
    ) {
        UpdateTaskData.Command command = new UpdateTaskData.Command(
            actor,
            taskId,
            request.name(),
            request.color()
        );
        UpdateTaskData.Result result = updateTaskUseCase.execute(command);

        return ApiResponse.success(
            UpdateTaskDto.Response.from(result),
            "기존 과목을 수정했습니다."
        );
    }

    @DeleteMapping("/{taskId}")
    public ApiResponse<Void> deleteTask(
        @AuthenticatedActor Actor actor,
        @PathVariable Long taskId
    ) {
        DeleteTaskData.Command command = new DeleteTaskData.Command(actor, taskId);
        deleteTaskUseCase.execute(command);

        return ApiResponse.success("기존 과목을 삭제했습니다.");
    }
}
