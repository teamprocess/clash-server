package com.process.clash.adapter.web.record.v2.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.record.v2.docs.controller.SubjectV2ControllerDocument;
import com.process.clash.adapter.web.record.v2.dto.CreateSubjectTaskV2Dto;
import com.process.clash.adapter.web.record.v2.dto.CreateSubjectV2Dto;
import com.process.clash.adapter.web.record.v2.dto.GetAllSubjectsV2Dto;
import com.process.clash.adapter.web.record.v2.dto.UpdateSubjectTaskV2Dto;
import com.process.clash.adapter.web.record.v2.dto.UpdateSubjectV2Dto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.v2.data.CreateSubjectTaskV2Data;
import com.process.clash.application.record.v2.data.CreateSubjectV2Data;
import com.process.clash.application.record.v2.data.DeleteSubjectTaskV2Data;
import com.process.clash.application.record.v2.data.DeleteSubjectV2Data;
import com.process.clash.application.record.v2.data.GetAllSubjectsV2Data;
import com.process.clash.application.record.v2.data.UpdateSubjectTaskV2Data;
import com.process.clash.application.record.v2.data.UpdateSubjectV2Data;
import com.process.clash.application.record.v2.port.in.CreateSubjectTaskV2UseCase;
import com.process.clash.application.record.v2.port.in.CreateSubjectV2UseCase;
import com.process.clash.application.record.v2.port.in.DeleteSubjectTaskV2UseCase;
import com.process.clash.application.record.v2.port.in.DeleteSubjectV2UseCase;
import com.process.clash.application.record.v2.port.in.GetAllSubjectsV2UseCase;
import com.process.clash.application.record.v2.port.in.UpdateSubjectTaskV2UseCase;
import com.process.clash.application.record.v2.port.in.UpdateSubjectV2UseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/record/subjects")
@RequiredArgsConstructor
public class SubjectV2Controller implements SubjectV2ControllerDocument {

    private final GetAllSubjectsV2UseCase getAllSubjectsV2UseCase;
    private final CreateSubjectV2UseCase createSubjectV2UseCase;
    private final UpdateSubjectV2UseCase updateSubjectV2UseCase;
    private final DeleteSubjectV2UseCase deleteSubjectV2UseCase;
    private final CreateSubjectTaskV2UseCase createSubjectTaskV2UseCase;
    private final UpdateSubjectTaskV2UseCase updateSubjectTaskV2UseCase;
    private final DeleteSubjectTaskV2UseCase deleteSubjectTaskV2UseCase;

    @GetMapping
    public ApiResponse<GetAllSubjectsV2Dto.Response> getAllSubjects(
        @AuthenticatedActor Actor actor
    ) {
        GetAllSubjectsV2Data.Command command = new GetAllSubjectsV2Data.Command(actor);
        GetAllSubjectsV2Data.Result result = getAllSubjectsV2UseCase.execute(command);

        return ApiResponse.success(
            GetAllSubjectsV2Dto.Response.from(result),
            "과목 그룹 목록을 조회했습니다."
        );
    }

    @PostMapping
    public ApiResponse<Void> createSubject(
        @AuthenticatedActor Actor actor,
        @Valid @RequestBody CreateSubjectV2Dto.Request request
    ) {
        CreateSubjectV2Data.Command command = request.toCommand(actor);
        createSubjectV2UseCase.execute(command);

        return ApiResponse.success("새로운 과목 그룹을 생성했습니다.");
    }

    @PatchMapping("/{subjectId}")
    public ApiResponse<UpdateSubjectV2Dto.Response> updateSubject(
        @AuthenticatedActor Actor actor,
        @PathVariable Long subjectId,
        @Valid @RequestBody UpdateSubjectV2Dto.Request request
    ) {
        UpdateSubjectV2Data.Command command = request.toCommand(actor, subjectId);
        UpdateSubjectV2Data.Result result = updateSubjectV2UseCase.execute(command);

        return ApiResponse.success(
            UpdateSubjectV2Dto.Response.from(result),
            "기존 과목 그룹을 수정했습니다."
        );
    }

    @DeleteMapping("/{subjectId}")
    public ApiResponse<Void> deleteSubject(
        @AuthenticatedActor Actor actor,
        @PathVariable Long subjectId
    ) {
        DeleteSubjectV2Data.Command command = new DeleteSubjectV2Data.Command(actor, subjectId);
        deleteSubjectV2UseCase.execute(command);

        return ApiResponse.success("기존 과목 그룹을 삭제했습니다.");
    }

    @PostMapping("/{subjectId}/tasks")
    public ApiResponse<Void> createTask(
        @AuthenticatedActor Actor actor,
        @PathVariable Long subjectId,
        @Valid @RequestBody CreateSubjectTaskV2Dto.Request request
    ) {
        CreateSubjectTaskV2Data.Command command = request.toCommand(actor, subjectId);
        createSubjectTaskV2UseCase.execute(command);

        return ApiResponse.success("새로운 세부 작업을 생성했습니다.");
    }

    @PatchMapping("/{subjectId}/tasks/{taskId}")
    public ApiResponse<UpdateSubjectTaskV2Dto.Response> updateTask(
        @AuthenticatedActor Actor actor,
        @PathVariable Long subjectId,
        @PathVariable Long taskId,
        @Valid @RequestBody UpdateSubjectTaskV2Dto.Request request
    ) {
        UpdateSubjectTaskV2Data.Command command = request.toCommand(actor, subjectId, taskId);
        UpdateSubjectTaskV2Data.Result result = updateSubjectTaskV2UseCase.execute(command);

        return ApiResponse.success(
            UpdateSubjectTaskV2Dto.Response.from(result),
            "기존 세부 작업을 수정했습니다."
        );
    }

    @DeleteMapping("/{subjectId}/tasks/{taskId}")
    public ApiResponse<Void> deleteTask(
        @AuthenticatedActor Actor actor,
        @PathVariable Long subjectId,
        @PathVariable Long taskId
    ) {
        DeleteSubjectTaskV2Data.Command command = new DeleteSubjectTaskV2Data.Command(actor, subjectId, taskId);
        deleteSubjectTaskV2UseCase.execute(command);

        return ApiResponse.success("기존 세부 작업을 삭제했습니다.");
    }
}
