package com.process.clash.adapter.web.record.v2.docs.controller;

import com.process.clash.adapter.web.record.v2.docs.request.CreateSubjectV2RequestDocument;
import com.process.clash.adapter.web.record.v2.docs.request.UpdateSubjectTaskV2RequestDocument;
import com.process.clash.adapter.web.record.v2.docs.request.UpdateSubjectV2RequestDocument;
import com.process.clash.adapter.web.record.v2.docs.response.CreateSubjectV2ResponseDocument;
import com.process.clash.adapter.web.record.v2.docs.response.DeleteSubjectTaskV2ResponseDocument;
import com.process.clash.adapter.web.record.v2.docs.response.DeleteSubjectV2ResponseDocument;
import com.process.clash.adapter.web.record.v2.docs.response.GetAllSubjectsV2ResponseDocument;
import com.process.clash.adapter.web.record.v2.docs.response.UpdateSubjectTaskV2ResponseDocument;
import com.process.clash.adapter.web.record.v2.docs.response.UpdateSubjectV2ResponseDocument;
import com.process.clash.adapter.web.record.v2.dto.CreateSubjectV2Dto;
import com.process.clash.adapter.web.record.v2.dto.GetAllSubjectsV2Dto;
import com.process.clash.adapter.web.record.v2.dto.UpdateSubjectTaskV2Dto;
import com.process.clash.adapter.web.record.v2.dto.UpdateSubjectV2Dto;
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

@Tag(name = "과목 그룹 API V2", description = "V2 과목 그룹/세부 작업 관리")
public interface SubjectV2ControllerDocument {

    @Operation(summary = "과목 그룹 목록 조회", description = "V2 과목 그룹과 하위 세부 작업 목록을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(
                schema = @Schema(implementation = GetAllSubjectsV2ResponseDocument.class),
                examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "message": "과목 그룹 목록을 조회했습니다.",
                      "data": {
                        "subjects": [
                          {
                            "id": 1,
                            "name": "백엔드 프로젝트",
                            "icon": "timer",
                            "studyTime": 5400,
                            "tasks": [
                              {
                                "id": 10,
                                "name": "ERD 설계",
                                "icon": "timer",
                                "studyTime": 1800
                              }
                            ]
                          }
                        ]
                      }
                    }
                    """)
            ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetAllSubjectsV2Dto.Response> getAllSubjects(
        @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "과목 그룹 생성", description = "새로운 V2 과목 그룹을 생성합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "생성 성공",
            content = @Content(
                schema = @Schema(implementation = CreateSubjectV2ResponseDocument.class),
                examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "message": "새로운 과목 그룹을 생성했습니다."
                    }
                    """)
            ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> createSubject(
        @Parameter(hidden = true) Actor actor,
        @RequestBody(description = "V2 과목 그룹 생성 요청", required = true,
            content = @Content(
                schema = @Schema(implementation = CreateSubjectV2RequestDocument.class),
                examples = @ExampleObject(value = """
                    {
                      "name": "백엔드 프로젝트"
                    }
                    """)
            ))
        CreateSubjectV2Dto.Request request
    );

    @Operation(summary = "과목 그룹 수정", description = "기존 V2 과목 그룹을 수정합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공",
            content = @Content(
                schema = @Schema(implementation = UpdateSubjectV2ResponseDocument.class),
                examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "message": "기존 과목 그룹을 수정했습니다.",
                      "data": {
                        "id": 1,
                        "name": "백엔드 프로젝트 심화",
                        "studyTime": 7200
                      }
                    }
                    """)
            ))
    })
    com.process.clash.adapter.web.common.ApiResponse<UpdateSubjectV2Dto.Response> updateSubject(
        @Parameter(hidden = true) Actor actor,
        @Parameter(description = "과목 그룹 ID", example = "1") @PathVariable Long subjectId,
        @RequestBody(description = "V2 과목 그룹 수정 요청", required = true,
            content = @Content(
                schema = @Schema(implementation = UpdateSubjectV2RequestDocument.class),
                examples = @ExampleObject(value = """
                    {
                      "name": "백엔드 프로젝트 심화"
                    }
                    """)
            ))
        UpdateSubjectV2Dto.Request request
    );

    @Operation(summary = "과목 그룹 삭제", description = "기존 V2 과목 그룹을 삭제합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공",
            content = @Content(
                schema = @Schema(implementation = DeleteSubjectV2ResponseDocument.class),
                examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "message": "기존 과목 그룹을 삭제했습니다."
                    }
                    """)
            ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> deleteSubject(
        @Parameter(hidden = true) Actor actor,
        @Parameter(description = "과목 그룹 ID", example = "1") @PathVariable Long subjectId
    );

    @Operation(summary = "세부 작업 수정", description = "과목 그룹 하위의 V2 세부 작업을 수정합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공",
            content = @Content(
                schema = @Schema(implementation = UpdateSubjectTaskV2ResponseDocument.class),
                examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "message": "기존 세부 작업을 수정했습니다.",
                      "data": {
                        "id": 10,
                        "subjectId": 1,
                        "name": "ERD 검토",
                        "studyTime": 2400
                      }
                    }
                    """)
            ))
    })
    com.process.clash.adapter.web.common.ApiResponse<UpdateSubjectTaskV2Dto.Response> updateTask(
        @Parameter(hidden = true) Actor actor,
        @Parameter(description = "과목 그룹 ID", example = "1") @PathVariable Long subjectId,
        @Parameter(description = "세부 작업 ID", example = "10") @PathVariable Long taskId,
        @RequestBody(description = "V2 세부 작업 수정 요청", required = true,
            content = @Content(
                schema = @Schema(implementation = UpdateSubjectTaskV2RequestDocument.class),
                examples = @ExampleObject(value = """
                    {
                      "name": "ERD 검토"
                    }
                    """)
            ))
        UpdateSubjectTaskV2Dto.Request request
    );

    @Operation(summary = "세부 작업 삭제", description = "과목 그룹 하위의 V2 세부 작업을 삭제합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공",
            content = @Content(
                schema = @Schema(implementation = DeleteSubjectTaskV2ResponseDocument.class),
                examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "message": "기존 세부 작업을 삭제했습니다."
                    }
                    """)
            ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> deleteTask(
        @Parameter(hidden = true) Actor actor,
        @Parameter(description = "과목 그룹 ID", example = "1") @PathVariable Long subjectId,
        @Parameter(description = "세부 작업 ID", example = "10") @PathVariable Long taskId
    );
}
