package com.process.clash.adapter.web.missions.docs.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.missions.dto.MissionSubmitDto;
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
                                        "explanation": "설명",
                                        "currentProgress": 1,
                                        "totalQuestion": 10,
                                        "correctChoiceId": 1
                                      },
                                      "message": "정답 제출을 성공했습니다.",
                                      "success": true
                                    }
                                    """)
                    )
            )
    })
    ApiResponse<MissionSubmitDto.Response> submitAnswer(
            @Parameter(description = "미션 ID", example = "1") @PathVariable Long missionId,
            @Parameter(description = "질문 ID", example = "1") @PathVariable Long questionId,
            @RequestBody(description = "제출할 선택지 ID", required = true,
                    content = @Content(
                            schema = @Schema(implementation = MissionSubmitDto.Request.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "submittedChoiceId": 1
                                    }
                                    """)
                    )
            ) MissionSubmitDto.Request request
    );
}