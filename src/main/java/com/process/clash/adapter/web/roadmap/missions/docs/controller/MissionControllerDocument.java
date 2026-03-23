package com.process.clash.adapter.web.roadmap.missions.docs.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.roadmap.missions.dto.MissionResultDto;
import com.process.clash.adapter.web.roadmap.missions.dto.MissionSubmitDto;
import com.process.clash.application.common.actor.Actor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "미션 API", description = "미션 관련 API")
public interface MissionControllerDocument {

    @Operation(summary = "미션 정답 제출", description = "특정 미션의 질문에 대한 정답을 제출합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "정답 제출 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "data": {
                                          "isCorrect": true,
                                          "explanation": "함수형 컴포넌트는 this.state를 사용할 수 없어서, 상태를 만들고 변경해 렌더링에 반영하려면 useState 훅을 사용합니다.",
                                          "currentProgress": 1,
                                          "totalQuestion": 5,
                                          "correctChoiceId": null,
                                          "isMissionCleared": false,
                                          "nextMissionId": null,
                                          "nextMissionOrderIndex": null,
                                          "isChapterCleared": false,
                                          "nextChapterId": null,
                                          "nextChapterOrderIndex": null
                                      },
                                      "message": "정답 제출을 성공했습니다.",
                                      "success": true
                                    }
                                    """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 선택지, 잠긴 챕터, 또는 잘못된 문제 순서",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "invalid_choice", value = """
                                            {
                                              "success": false,
                                              "error": {
                                                "code": "INVALID_CHOICE",
                                                "message": "유효하지 않은 선택지입니다.",
                                                "timestamp": "2025-01-02T10:00:00"
                                              }
                                            }
                                            """),
                                    @ExampleObject(name = "chapter_locked", value = """
                                            {
                                              "success": false,
                                              "error": {
                                                "code": "CHAPTER_LOCKED",
                                                "message": "챕터가 잠겨 있어 접근할 수 없습니다.",
                                                "timestamp": "2025-01-02T10:00:00"
                                              }
                                            }
                                            """),
                                    @ExampleObject(name = "invalid_question_order", value = """
                                            {
                                              "success": false,
                                              "error": {
                                                "code": "INVALID_QUESTION_ORDER",
                                                "message": "문제를 순서대로 제출해야 합니다.",
                                                "timestamp": "2025-01-02T10:00:00"
                                              }
                                            }
                                            """)
                            }
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "미션, 질문, 또는 챕터를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "mission_not_found", value = """
                                            {
                                              "success": false,
                                              "error": {
                                                "code": "MISSION_NOT_FOUND",
                                                "message": "미션을 찾을 수 없습니다.",
                                                "timestamp": "2025-01-02T10:00:00"
                                              }
                                            }
                                            """),
                                    @ExampleObject(name = "question_not_found", value = """
                                            {
                                              "success": false,
                                              "error": {
                                                "code": "QUESTION_NOT_FOUND",
                                                "message": "질문을 찾을 수 없습니다.",
                                                "timestamp": "2025-01-02T10:00:00"
                                              }
                                            }
                                            """),
                                    @ExampleObject(name = "chapter_not_found", value = """
                                            {
                                              "success": false,
                                              "error": {
                                                "code": "CHAPTER_NOT_FOUND",
                                                "message": "챕터를 찾을 수 없습니다.",
                                                "timestamp": "2025-01-02T10:00:00"
                                              }
                                            }
                                            """)
                            }
                    )
            )
    })
    ApiResponse<MissionSubmitDto.Response> submitAnswer(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "미션 ID", example = "1") @PathVariable Long missionId,
            @Parameter(description = "질문 ID", example = "1") @PathVariable Long questionId,
            @RequestBody(description = "미션 정답 제출 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = MissionSubmitDto.Request.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "submittedChoiceId": 1
                                    }
                                    """)
                    ))
            MissionSubmitDto.Request request
    );

    @Operation(summary = "미션 결과 조회", description = "특정 미션의 완료 결과를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "미션 결과 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "data": {
                                          "missionId": 1,
                                          "isCleared": true,
                                          "correctCount": 5,
                                          "totalCount": 5,
                                          "nextMissionId": 2,
                                          "nextMissionOrderIndex": 1,
                                          "nextChapterId": null,
                                          "nextChapterOrderIndex": null
                                      },
                                      "message": "미션 결과 조회를 성공했습니다.",
                                      "success": true
                                    }
                                    """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "미션 또는 챕터를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "mission_not_found", value = """
                                            {
                                              "success": false,
                                              "error": {
                                                "code": "MISSION_NOT_FOUND",
                                                "message": "미션을 찾을 수 없습니다.",
                                                "timestamp": "2025-01-02T10:00:00"
                                              }
                                            }
                                            """),
                                    @ExampleObject(name = "chapter_not_found", value = """
                                            {
                                              "success": false,
                                              "error": {
                                                "code": "CHAPTER_NOT_FOUND",
                                                "message": "챕터를 찾을 수 없습니다.",
                                                "timestamp": "2025-01-02T10:00:00"
                                              }
                                            }
                                            """)
                            }
                    )
            )
    })
    ApiResponse<MissionResultDto.Response> getResult(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "미션 ID", example = "1") @PathVariable Long missionId
    );

    @Operation(summary = "미션 초기화", description = "특정 미션의 진행 상황을 초기화합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "미션 초기화 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "message": "미션 진행 상황이 초기화되었습니다. 다시 시작합니다.",
                                      "success": true
                                    }
                                    """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "미션을 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "error": {
                                        "code": "MISSION_NOT_FOUND",
                                        "message": "미션을 찾을 수 없습니다.",
                                        "timestamp": "2025-01-02T10:00:00"
                                      }
                                    }
                                    """)
                    )
            )
    })
    ApiResponse<Void> resetMission(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "미션 ID", example = "1") @PathVariable Long missionId
    );
}
