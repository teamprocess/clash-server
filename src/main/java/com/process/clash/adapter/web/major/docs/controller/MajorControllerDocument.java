package com.process.clash.adapter.web.major.docs.controller;

import com.process.clash.adapter.web.major.docs.request.MajorTestSubmitRequestDoc;
import com.process.clash.adapter.web.major.docs.response.GetMajorQuestionResponseDoc;
import com.process.clash.adapter.web.major.docs.response.MajorTestSubmitResponseDoc;
import com.process.clash.adapter.web.major.dto.GetMajorQuestionDto;
import com.process.clash.adapter.web.major.dto.MajorTestSubmitDto;
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

@Tag(name = "전공 검사 API", description = "전공 성향 검사")
public interface MajorControllerDocument {

    @Operation(summary = "전공 질문 조회", description = "전공 성향 검사 질문 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "질문 조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetMajorQuestionResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "전공 성향 검사 조회를 성공했습니다.",
                                      "data": {
                                        "majorQuestions": [
                                          {
                                            "id": 1,
                                            "content": "어떤 분야에 흥미가 있나요?",
                                            "weight": {
                                              "web": 3,
                                              "app": 1,
                                              "server": 5,
                                              "ai": 2,
                                              "game": 0
                                            }
                                          }
                                        ]
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetMajorQuestionDto.Response> getMajorQuestion(
            @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "전공 검사 제출", description = "전공 성향 검사 결과를 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "제출 성공",
                    content = @Content(
                            schema = @Schema(implementation = MajorTestSubmitResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "전공 성향 검사 결과 저장을 성공했습니다."
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> testResultSubmit(
            @Parameter(hidden = true) Actor actor,
            @RequestBody(description = "전공 검사 결과", required = true,
                    content = @Content(
                            schema = @Schema(implementation = MajorTestSubmitRequestDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "major": "SERVER"
                                    }
                                    """)
                    ))
            MajorTestSubmitDto.Request request
    );
}
