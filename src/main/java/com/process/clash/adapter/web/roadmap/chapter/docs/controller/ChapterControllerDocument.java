package com.process.clash.adapter.web.roadmap.chapter.docs.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.roadmap.chapter.dto.GetChapterDetailsDto;
import com.process.clash.application.common.actor.Actor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "챕터 API", description = "챕터 관련 API")
public interface ChapterControllerDocument {

    @Operation(summary = "챕터 상세 조회", description = "특정 챕터의 상세 정보를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "챕터 상세 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "data": {
                                          "chapterId": 1,
                                          "title": "함수형 컴포넌트",
                                          "description": "함수형 컴포넌트에 대해 학습합니다.",
                                          "currentMissionId": 1,
                                          "currentQuestionId": 1,
                                          "currentQuestionIndex": 0,
                                          "totalQuestions": 5,
                                          "missions": []
                                      },
                                      "message": "챕터 상세 조회를 성공했습니다.",
                                      "success": true
                                    }
                                    """)
                    )
            )
    })
    ApiResponse<GetChapterDetailsDto.Response> getChapterDetails(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "챕터 ID", example = "1") @PathVariable Long chapterId
    );
}
