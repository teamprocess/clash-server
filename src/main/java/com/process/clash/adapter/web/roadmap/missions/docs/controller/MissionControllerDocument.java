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
            )
    })
    ApiResponse<MissionSubmitDto.Response> submitAnswer(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "미션 ID", example = "1") @PathVariable Long missionId,
            @Parameter(description = "질문 ID", example = "1") @PathVariable Long questionId,
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
                                          "nextChapterId": null,
                                          "sectionOrderIndex": 0,
                                          "chapterOrderIndex": 1,
                                          "missionOrderIndex": 0
                                      },
                                      "message": "미션 결과 조회를 성공했습니다.",
                                      "success": true
                                    }
                                    """)
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
                                      "data": null,
                                      "message": "미션 진행 상황이 초기화되었습니다. 다시 시작합니다.",
                                      "success": true
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
