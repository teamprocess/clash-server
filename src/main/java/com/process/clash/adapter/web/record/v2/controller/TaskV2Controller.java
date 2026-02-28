package com.process.clash.adapter.web.record.v2.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.record.v2.dto.CreateSubjectTaskV2Dto;
import com.process.clash.adapter.web.record.v2.dto.GetAllTasksV2Dto;
import com.process.clash.adapter.web.record.v2.dto.UpdateTaskCompletionV2Dto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.v2.data.CreateSubjectTaskV2Data;
import com.process.clash.application.record.v2.data.GetAllTasksV2Data;
import com.process.clash.application.record.v2.data.UpdateTaskCompletionV2Data;
import com.process.clash.application.record.v2.port.in.CreateSubjectTaskV2UseCase;
import com.process.clash.application.record.v2.port.in.GetAllTasksV2UseCase;
import com.process.clash.application.record.v2.port.in.UpdateTaskCompletionV2UseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/record/tasks")
@RequiredArgsConstructor
public class TaskV2Controller {

    private final GetAllTasksV2UseCase getAllTasksV2UseCase;
    private final CreateSubjectTaskV2UseCase createSubjectTaskV2UseCase;
    private final UpdateTaskCompletionV2UseCase updateTaskCompletionV2UseCase;

    @GetMapping
    public ApiResponse<GetAllTasksV2Dto.Response> getAllTasks(
        @AuthenticatedActor Actor actor
    ) {
        GetAllTasksV2Data.Command command = new GetAllTasksV2Data.Command(actor);
        GetAllTasksV2Data.Result result = getAllTasksV2UseCase.execute(command);

        return ApiResponse.success(
            GetAllTasksV2Dto.Response.from(result),
            "세부 작업 목록을 조회했습니다."
        );
    }

    @PostMapping
    public ApiResponse<Void> createTask(
        @AuthenticatedActor Actor actor,
        @Valid @RequestBody CreateSubjectTaskV2Dto.Request request
    ) {
        CreateSubjectTaskV2Data.Command command = request.toCommand(actor);
        createSubjectTaskV2UseCase.execute(command);

        return ApiResponse.success("새로운 세부 작업을 생성했습니다.");
    }

    @PatchMapping("/{taskId}/completion")
    public ApiResponse<UpdateTaskCompletionV2Dto.Response> updateTaskCompletion(
        @AuthenticatedActor Actor actor,
        @PathVariable Long taskId,
        @Valid @RequestBody UpdateTaskCompletionV2Dto.Request request
    ) {
        UpdateTaskCompletionV2Data.Command command = request.toCommand(actor, taskId);
        UpdateTaskCompletionV2Data.Result result = updateTaskCompletionV2UseCase.execute(command);

        return ApiResponse.success(
            UpdateTaskCompletionV2Dto.Response.from(result),
            "세부 작업 완료 상태를 변경했습니다."
        );
    }
}
