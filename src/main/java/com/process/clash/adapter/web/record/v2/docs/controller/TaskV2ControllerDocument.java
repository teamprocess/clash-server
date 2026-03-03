package com.process.clash.adapter.web.record.v2.docs.controller;

import com.process.clash.adapter.web.record.v2.docs.request.CreateSubjectTaskV2RequestDocument;
import com.process.clash.adapter.web.record.v2.docs.request.UpdateTaskCompletionV2RequestDocument;
import com.process.clash.adapter.web.record.v2.docs.response.CreateTaskV2ResponseDocument;
import com.process.clash.adapter.web.record.v2.docs.response.GetAllTasksV2ResponseDocument;
import com.process.clash.adapter.web.record.v2.docs.response.UpdateTaskCompletionV2ResponseDocument;
import com.process.clash.adapter.web.record.v2.dto.CreateSubjectTaskV2Dto;
import com.process.clash.adapter.web.record.v2.dto.GetAllTasksV2Dto;
import com.process.clash.adapter.web.record.v2.dto.UpdateTaskCompletionV2Dto;
import com.process.clash.application.common.actor.Actor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "세부 작업 API V2", description = "V2 세부 작업 관리")
public interface TaskV2ControllerDocument {

    @Operation(summary = "세부 작업 목록 조회", description = "세부 작업 목록을 조회합니다. subjectId가 없는 작업이 먼저 오고, 이후 subjectId 내림차순으로 정렬됩니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(
                schema = @Schema(implementation = GetAllTasksV2ResponseDocument.class),
                examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "message": "세부 작업 목록을 조회했습니다.",
                      "data": {
                        "tasks": [
                          {
                            "id": 31,
                            "subjectId": null,
                            "name": "리팩터링",
                            "icon": "timer",
                            "completed": false,
                            "studyTime": 1800
                          },
                          {
                            "id": 25,
                            "subjectId": 12,
                            "name": "ERD 설계",
                            "icon": "timer",
                            "completed": true,
                            "studyTime": 3600
                          }
                        ]
                      }
                    }
                    """)
            ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetAllTasksV2Dto.Response> getAllTasks(
        @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "세부 작업 생성", description = "새로운 V2 세부 작업을 생성합니다. subjectId 없이도 생성할 수 있습니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "생성 성공",
            content = @Content(
                schema = @Schema(implementation = CreateTaskV2ResponseDocument.class),
                examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "message": "새로운 세부 작업을 생성했습니다."
                    }
                    """)
            ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> createTask(
        @Parameter(hidden = true) Actor actor,
        @RequestBody(description = "V2 세부 작업 생성 요청", required = true,
            content = @Content(
                schema = @Schema(implementation = CreateSubjectTaskV2RequestDocument.class),
                examples = @ExampleObject(value = """
                    {
                      "subjectId": 12,
                      "name": "ERD 설계"
                    }
                    """)
            ))
        CreateSubjectTaskV2Dto.Request request
    );

    @Operation(summary = "세부 작업 완료 상태 변경", description = "세부 작업의 완료/완료 취소 상태를 변경합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "변경 성공",
            content = @Content(
                schema = @Schema(implementation = UpdateTaskCompletionV2ResponseDocument.class),
                examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "message": "세부 작업 완료 상태를 변경했습니다.",
                      "data": {
                        "id": 31,
                        "subjectId": null,
                        "name": "리팩터링",
                        "completed": true
                      }
                    }
                    """)
            ))
    })
    com.process.clash.adapter.web.common.ApiResponse<UpdateTaskCompletionV2Dto.Response> updateTaskCompletion(
        @Parameter(hidden = true) Actor actor,
        @Parameter(description = "세부 작업 ID", example = "31") @PathVariable Long taskId,
        @RequestBody(description = "V2 세부 작업 완료 상태 변경 요청", required = true,
            content = @Content(
                schema = @Schema(implementation = UpdateTaskCompletionV2RequestDocument.class),
                examples = @ExampleObject(value = """
                    {
                      "completed": true
                    }
                    """)
            ))
        UpdateTaskCompletionV2Dto.Request request
    );
}
