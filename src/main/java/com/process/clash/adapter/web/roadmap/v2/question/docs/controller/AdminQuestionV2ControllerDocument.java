package com.process.clash.adapter.web.roadmap.v2.question.docs.controller;

import com.process.clash.adapter.web.roadmap.v2.question.docs.request.CreateQuestionV2RequestDocument;
import com.process.clash.adapter.web.roadmap.v2.question.docs.request.UpdateQuestionV2RequestDocument;
import com.process.clash.adapter.web.roadmap.v2.question.docs.response.CreateQuestionV2ResponseDocument;
import com.process.clash.adapter.web.roadmap.v2.question.docs.response.DeleteQuestionV2ResponseDocument;
import com.process.clash.adapter.web.roadmap.v2.question.docs.response.UpdateQuestionV2ResponseDocument;
import com.process.clash.adapter.web.roadmap.v2.question.dto.CreateQuestionV2Dto;
import com.process.clash.adapter.web.roadmap.v2.question.dto.UpdateQuestionV2Dto;
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

@Tag(name = "문제 V2 관리 API", description = "문제 생성/수정/삭제 (V2)")
public interface AdminQuestionV2ControllerDocument {

    @Operation(summary = "문제 생성", description = "신규 문제를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공",
                    content = @Content(
                            schema = @Schema(implementation = CreateQuestionV2ResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "문제 생성을 성공했습니다.",
                                      "data": {
                                        "questionId": 1,
                                        "chapterId": 1,
                                        "content": "자바의 특징이 아닌 것은?",
                                        "explanation": "자바는 객체지향 언어입니다.",
                                        "orderIndex": 0,
                                        "difficulty": 1
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<CreateQuestionV2Dto.Response> createQuestion(
            @Parameter(hidden = true) Actor actor,
            @RequestBody(description = "문제 생성 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = CreateQuestionV2RequestDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "chapterId": 1,
                                      "content": "자바의 특징이 아닌 것은?",
                                      "explanation": "자바는 객체지향 언어입니다.",
                                      "orderIndex": 0,
                                      "difficulty": 1
                                    }
                                    """)
                    ))
            CreateQuestionV2Dto.Request request
    );

    @Operation(summary = "문제 수정", description = "기존 문제를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(
                            schema = @Schema(implementation = UpdateQuestionV2ResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "문제 수정을 성공했습니다.",
                                      "data": {
                                        "questionId": 1,
                                        "content": "자바의 주요 특징은?",
                                        "explanation": "자바는 객체지향, 플랫폼 독립적, 멀티스레드를 지원합니다.",
                                        "orderIndex": 0,
                                        "difficulty": 2
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<UpdateQuestionV2Dto.Response> updateQuestion(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "문제 ID", example = "1") @PathVariable Long questionId,
            @RequestBody(description = "문제 수정 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = UpdateQuestionV2RequestDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "content": "자바의 주요 특징은?",
                                      "explanation": "자바는 객체지향, 플랫폼 독립적, 멀티스레드를 지원합니다.",
                                      "orderIndex": 0,
                                      "difficulty": 2
                                    }
                                    """)
                    ))
            UpdateQuestionV2Dto.Request request
    );

    @Operation(summary = "문제 삭제", description = "문제를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공",
                    content = @Content(
                            schema = @Schema(implementation = DeleteQuestionV2ResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "문제 삭제를 성공했습니다. (연관된 선택지도 함께 삭제됩니다)"
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> deleteQuestion(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "문제 ID", example = "1") @PathVariable Long questionId
    );
}
