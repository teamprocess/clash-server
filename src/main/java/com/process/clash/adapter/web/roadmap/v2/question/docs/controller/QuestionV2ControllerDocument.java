package com.process.clash.adapter.web.roadmap.v2.question.docs.controller;

import com.process.clash.adapter.web.roadmap.v2.question.docs.request.SubmitQuestionV2RequestDoc;
import com.process.clash.adapter.web.roadmap.v2.question.docs.response.GetChapterV2ResultResponseDoc;
import com.process.clash.adapter.web.roadmap.v2.question.docs.response.SubmitQuestionV2ResponseDoc;
import com.process.clash.adapter.web.roadmap.v2.question.dto.GetChapterV2ResultDto;
import com.process.clash.adapter.web.roadmap.v2.question.dto.SubmitQuestionV2AnswerDto;
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

@Tag(name = "문제 V2 API", description = "문제 풀이 및 챕터 관리 (V2 - Chapter 단위)")
public interface QuestionV2ControllerDocument {

    @Operation(summary = "답안 제출", description = "문제의 답안을 제출합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "제출 성공",
                    content = @Content(
                            schema = @Schema(implementation = SubmitQuestionV2ResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "답안 제출을 성공했습니다.",
                                      "data": {
                                        "isCorrect": true,
                                        "explanation": "정답입니다! 자바는 객체지향 언어입니다.",
                                        "currentProgress": 3,
                                        "totalQuestion": 5,
                                        "correctChoiceId": 1,
                                        "isChapterCleared": false,
                                        "nextChapterId": null,
                                        "nextChapterOrderIndex": null
                                      }
                                    }
                                    """)
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 선택지 또는 챕터 잠김",
                    content = @Content(
                            schema = @Schema(implementation = com.process.clash.adapter.web.common.ApiResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "error": {
                                        "code": "CHAPTER_V2_LOCKED",
                                        "message": "챕터가 잠겨 있어 접근할 수 없습니다.",
                                        "timestamp": "2025-01-02T10:00:00"
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<SubmitQuestionV2AnswerDto.Response> submitAnswer(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "문제 ID", example = "1") @PathVariable Long questionId,
            @RequestBody(description = "답안 제출 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = SubmitQuestionV2RequestDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "submittedChoiceId": 1
                                    }
                                    """)
                    ))
            SubmitQuestionV2AnswerDto.Request request
    );

    @Operation(summary = "챕터 결과 조회", description = "챕터의 진행 결과를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetChapterV2ResultResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "챕터 결과 조회를 성공했습니다.",
                                      "data": {
                                        "isCleared": true,
                                        "correctCount": 5,
                                        "totalCount": 5,
                                        "scorePercentage": 100
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetChapterV2ResultDto.Response> getChapterResult(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "챕터 ID", example = "1") @PathVariable Long chapterId
    );

    @Operation(summary = "챕터 초기화", description = "챕터의 진행도를 초기화합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "초기화 성공",
                    content = @Content(
                            schema = @Schema(implementation = com.process.clash.adapter.web.common.ApiResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "챕터 진행도 초기화를 성공했습니다."
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> resetChapter(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "챕터 ID", example = "1") @PathVariable Long chapterId
    );
}
