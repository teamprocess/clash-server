package com.process.clash.adapter.web.roadmap.section.docs.controller;

import com.process.clash.adapter.web.roadmap.section.docs.response.GetSectionDetailsResponseDoc;
import com.process.clash.adapter.web.roadmap.section.docs.response.GetSectionPreviewResponseDoc;
import com.process.clash.adapter.web.roadmap.section.docs.response.GetSectionsResponseDoc;
import com.process.clash.adapter.web.roadmap.section.dto.GetSectionDetailsDto;
import com.process.clash.adapter.web.roadmap.section.dto.GetSectionPreviewDto;
import com.process.clash.adapter.web.roadmap.section.dto.GetSectionsDto;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.common.enums.Major;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "로드맵 조회 API", description = "로드맵 목록/상세 조회")
public interface SectionControllerDocument {

    @Operation(summary = "로드맵 목록 조회", description = "전공 기준으로 로드맵 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetSectionsResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "로드맵 목록 조회를 성공했습니다.",
                                      "data": {
                                        "sections": [
                                          {
                                            "id": 1,
                                            "title": "스프링 입문",
                                            "category": "백엔드",
                                            "completed": false,
                                            "locked": false
                                          }
                                        ],
                                        "categories": [
                                          "백엔드"
                                        ]
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetSectionsDto.Response> getSections(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "전공", example = "SERVER", required = true)
            @RequestParam Major major
    );

    @Operation(summary = "로드맵 미리보기 조회", description = "로드맵의 미리보기 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetSectionPreviewResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "로드맵 미리보기 조회를 성공했습니다.",
                                      "data": {
                                        "id": 1,
                                        "title": "스프링 입문",
                                        "description": "스프링 핵심 개념을 학습합니다.",
                                        "totalChapters": 3,
                                        "chapters": [
                                          {
                                            "id": 1,
                                            "title": "스프링 기초",
                                            "description": "DI/AOP"
                                          }
                                        ],
                                        "keyPoints": [
                                          "DI",
                                          "AOP"
                                        ]
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetSectionPreviewDto.Response> getSectionPreview(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "로드맵 ID", example = "1") @PathVariable Long sectionId
    );

    @Operation(summary = "로드맵 상세 조회", description = "로드맵 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetSectionDetailsResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "로드맵 상세 조회를 성공했습니다.",
                                      "data": {
                                        "sectionId": 1,
                                        "sectionTitle": "스프링 입문",
                                        "totalChapters": 3,
                                        "currentChapterId": 1,
                                        "currentOrderIndex": 0,
                                        "chapters": [
                                          {
                                            "id": 1,
                                            "title": "스프링 기초",
                                            "difficulty": 1,
                                            "missions": [
                                              {
                                                "id": 1,
                                                "title": "DI 이해",
                                                "questions": [
                                                  {
                                                    "id": 1,
                                                    "title": "DI란 무엇인가요?",
                                                    "choices": [
                                                      {
                                                        "id": 1,
                                                        "content": "의존성 주입"
                                                      }
                                                    ]
                                                  }
                                                ]
                                              }
                                            ]
                                          }
                                        ]
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetSectionDetailsDto.Response> getSectionDetails(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "로드맵 ID", example = "1") @PathVariable Long sectionId
    );
}
