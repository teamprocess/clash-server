package com.process.clash.adapter.web.record.docs.controller;

import com.process.clash.adapter.web.record.docs.request.CreateTaskRequestDocument;
import com.process.clash.adapter.web.record.docs.request.UpdateTaskRequestDocument;
import com.process.clash.adapter.web.record.docs.response.CreateTaskResponseDocument;
import com.process.clash.adapter.web.record.docs.response.DeleteTaskResponseDocument;
import com.process.clash.adapter.web.record.docs.response.GetAllTasksResponseDocument;
import com.process.clash.adapter.web.record.docs.response.UpdateTaskResponseDocument;
import com.process.clash.adapter.web.record.dto.CreateTaskDto;
import com.process.clash.adapter.web.record.dto.GetAllTasksDto;
import com.process.clash.adapter.web.record.dto.UpdateTaskDto;
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

@Tag(name = "과목 API", description = "과목 관리")
public interface TaskControllerDocument {

    @Operation(summary = "과목 목록 조회", description = "등록된 과목 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetAllTasksResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "과목 목록을 조회했습니다.",
                                      "data": {
                                        "tasks": [
                                          {
                                            "id": 1,
                                            "name": "자료구조",
                                            "icon": "timer",
                                            "studyTime": 3600
                                          }
                                        ]
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetAllTasksDto.Response> getAllTasks(
            @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "과목 생성", description = "새로운 과목을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "생성 성공",
                    content = @Content(
                            schema = @Schema(implementation = CreateTaskResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "새로운 과목을 생성했습니다."
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> createTask(
            @Parameter(hidden = true) Actor actor,
            @RequestBody(description = "과목 생성 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = CreateTaskRequestDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "name": "알고리즘"
                                    }
                                    """)
                    ))
            CreateTaskDto.Request request
    );

    @Operation(summary = "과목 수정", description = "기존 과목을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(
                            schema = @Schema(implementation = UpdateTaskResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "기존 과목을 수정했습니다.",
                                      "data": {
                                        "id": 1,
                                        "name": "알고리즘 심화",
                                        "studyTime": 4200
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<UpdateTaskDto.Response> updateTask(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "과목 ID", example = "1") @PathVariable Long taskId,
            @RequestBody(description = "과목 수정 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = UpdateTaskRequestDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "name": "알고리즘 심화"
                                    }
                                    """)
                    ))
            UpdateTaskDto.Request request
    );

    @Operation(summary = "과목 삭제", description = "기존 과목을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공",
                    content = @Content(
                            schema = @Schema(implementation = DeleteTaskResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "기존 과목을 삭제했습니다."
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> deleteTask(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "과목 ID", example = "1") @PathVariable Long taskId
    );
}
