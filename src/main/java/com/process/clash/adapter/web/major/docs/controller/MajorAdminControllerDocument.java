package com.process.clash.adapter.web.major.docs.controller;

import com.process.clash.adapter.web.major.docs.request.PostMajorQuestionRequestDocument;
import com.process.clash.adapter.web.major.docs.request.UpdateMajorQuestionRequestDocument;
import com.process.clash.adapter.web.major.docs.response.DeleteMajorQuestionResponseDocument;
import com.process.clash.adapter.web.major.docs.response.PostMajorQuestionResponseDocument;
import com.process.clash.adapter.web.major.docs.response.UpdateMajorQuestionResponseDocument;
import com.process.clash.adapter.web.major.dto.PostMajorQuestionDto;
import com.process.clash.adapter.web.major.dto.UpdateMajorQuestionDto;
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

@Tag(name = "전공 관리 API", description = "전공 질문 관리")
public interface MajorAdminControllerDocument {

    @Operation(summary = "전공 질문 생성", description = "전공 성향 검사 질문을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공",
                    content = @Content(
                            schema = @Schema(implementation = PostMajorQuestionResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "전공 질문이 성공적으로 생성되었습니다.",
                                      "data": {
                                        "questionId": 10,
                                        "content": "관심있는 분야를 선택해주세요",
                                        "weight": {
                                          "web": 2,
                                          "app": 1,
                                          "server": 4,
                                          "ai": 1,
                                          "game": 0
                                        },
                                        "createdAt": "2025-01-01T12:00:00"
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<PostMajorQuestionDto.Response> postMajorQuestion(
            @Parameter(hidden = true) Actor actor,
            @RequestBody(description = "전공 질문 생성 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = PostMajorQuestionRequestDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "content": "관심있는 분야를 선택해주세요",
                                      "weight": {
                                        "web": 2,
                                        "app": 1,
                                        "server": 4,
                                        "ai": 1,
                                        "game": 0
                                      }
                                    }
                                    """)
                    ))
            PostMajorQuestionDto.Request request
    );

    @Operation(summary = "전공 질문 수정", description = "전공 성향 검사 질문을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(
                            schema = @Schema(implementation = UpdateMajorQuestionResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "전공 성향 검사 질문 수정을 성공했습니다.",
                                      "data": {
                                        "questionId": 10,
                                        "content": "더 흥미로운 분야를 선택해주세요",
                                        "weight": {
                                          "web": 1,
                                          "app": 2,
                                          "server": 3,
                                          "ai": 2,
                                          "game": 1
                                        },
                                        "updatedAt": "2025-01-02T09:30:00"
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<UpdateMajorQuestionDto.Response> updateMajorQuestion(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "질문 ID", example = "1") @PathVariable Long questionId,
            @RequestBody(description = "전공 질문 수정 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = UpdateMajorQuestionRequestDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "content": "더 흥미로운 분야를 선택해주세요",
                                      "weight": {
                                        "web": 1,
                                        "app": 2,
                                        "server": 3,
                                        "ai": 2,
                                        "game": 1
                                      }
                                    }
                                    """)
                    ))
            UpdateMajorQuestionDto.Request request
    );

    @Operation(summary = "전공 질문 삭제", description = "전공 성향 검사 질문을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공",
                    content = @Content(
                            schema = @Schema(implementation = DeleteMajorQuestionResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "전공 성향 검사 질문 삭제를 성공했습니다."
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> deleteMajorQuestion(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "질문 ID", example = "1") @PathVariable Long questionId
    );
}
