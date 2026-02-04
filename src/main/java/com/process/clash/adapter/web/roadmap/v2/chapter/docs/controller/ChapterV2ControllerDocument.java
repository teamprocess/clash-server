package com.process.clash.adapter.web.roadmap.v2.chapter.docs.controller;

import com.process.clash.adapter.web.roadmap.v2.chapter.docs.response.GetChapterV2DetailsResponseDoc;
import com.process.clash.adapter.web.roadmap.v2.chapter.dto.GetChapterV2DetailsDto;
import com.process.clash.application.common.actor.Actor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "챕터 V2 API", description = "챕터 조회 (V2 - Mission 레이어 제거)")
public interface ChapterV2ControllerDocument {

    @Operation(summary = "챕터 상세 조회", description = "챕터 상세 정보와 문제 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetChapterV2DetailsResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "챕터 상세 조회를 성공했습니다.",
                                      "data": {
                                        "chapterId": 1,
                                        "title": "자바 기초",
                                        "description": "자바의 기본 문법을 학습합니다.",
                                        "orderIndex": 0,
                                        "questions": [
                                          {
                                            "questionId": 1,
                                            "content": "자바의 특징이 아닌 것은?",
                                            "explanation": "자바는 객체지향 언어입니다.",
                                            "orderIndex": 0,
                                            "difficulty": 1,
                                            "choices": [
                                              {
                                                "choiceId": 1,
                                                "content": "객체지향"
                                              },
                                              {
                                                "choiceId": 2,
                                                "content": "절차지향"
                                              }
                                            ]
                                          }
                                        ],
                                        "totalQuestions": 5,
                                        "currentQuestionIndex": 2,
                                        "correctCount": 2,
                                        "isCleared": false
                                      }
                                    }
                                    """)
                    )),
            @ApiResponse(responseCode = "404", description = "챕터를 찾을 수 없음",
                    content = @Content(
                            schema = @Schema(implementation = com.process.clash.adapter.web.common.ApiResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "error": {
                                        "code": "CHAPTER_V2_NOT_FOUND",
                                        "message": "챕터를 찾을 수 없습니다.",
                                        "timestamp": "2025-01-02T10:00:00"
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetChapterV2DetailsDto.Response> getChapterDetails(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "챕터 ID", example = "1") @PathVariable Long chapterId
    );
}
