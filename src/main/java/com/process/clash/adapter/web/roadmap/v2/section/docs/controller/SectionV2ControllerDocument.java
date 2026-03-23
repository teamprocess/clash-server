package com.process.clash.adapter.web.roadmap.v2.section.docs.controller;

import com.process.clash.adapter.web.roadmap.v2.section.docs.response.GetSectionV2DetailsResponseDocument;
import com.process.clash.adapter.web.roadmap.v2.section.docs.response.GetSectionV2ListResponseDocument;
import com.process.clash.adapter.web.roadmap.v2.section.docs.response.GetSectionV2PreviewResponseDocument;
import com.process.clash.adapter.web.roadmap.v2.section.dto.GetSectionV2DetailsDto;
import com.process.clash.adapter.web.roadmap.v2.section.dto.GetSectionV2ListDto;
import com.process.clash.adapter.web.roadmap.v2.section.dto.GetSectionV2PreviewDto;
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

@Tag(name = "로드맵 조회 API V2", description = "로드맵 목록/상세 조회 (V2 - Mission 레이어 제거)")
public interface SectionV2ControllerDocument {

    @Operation(summary = "로드맵 목록 조회 (V2)", description = "전공 기준으로 로드맵 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetSectionV2ListResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "로드맵 목록 조회를 성공했습니다.",
                                      "data": {
                                        "sections": [
                                          {
                                            "id": 1,
                                            "title": "스프링 입문",
                                            "categoryId": 10,
                                            "categoryImageUrl": "https://example.com/image.png",
                                            "completed": false,
                                            "locked": false
                                          }
                                        ],
                                        "categories": [10],
                                        "completedSections": 1,
                                        "totalSections": 5
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetSectionV2ListDto.Response> getSections(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "전공", example = "SERVER", required = true)
            @RequestParam Major major
    );

    @Operation(summary = "로드맵 상세 조회 (V2)", description = "로드맵 상세 정보와 챕터 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetSectionV2DetailsResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "로드맵 상세 조회를 성공했습니다.",
                                      "data": {
                                        "sectionId": 1,
                                        "sectionTitle": "스프링 입문",
                                        "completed": false,
                                        "totalChapters": 3,
                                        "currentChapterId": 1,
                                        "currentOrderIndex": 0,
                                        "chapters": [
                                          {
                                            "chapterId": 1,
                                            "title": "자바 기초",
                                            "description": "자바의 기본 문법을 학습합니다.",
                                            "orderIndex": 0,
                                            "studyMaterialUrl": "https://example.com/material"
                                          }
                                        ]
                                      }
                                    }
                                    """)
                    )),
            @ApiResponse(responseCode = "404", description = "로드맵을 찾을 수 없음",
                    content = @Content(
                            schema = @Schema(implementation = com.process.clash.adapter.web.common.ApiResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "error": {
                                        "code": "SECTION_NOT_FOUND",
                                        "message": "로드맵을 찾을 수 없습니다.",
                                        "timestamp": "2025-01-02T10:00:00"
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetSectionV2DetailsDto.Response> getSectionDetails(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "로드맵 ID", example = "1") @PathVariable Long sectionId
    );

    @Operation(summary = "로드맵 미리보기 조회 (V2)", description = "로드맵의 미리보기 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetSectionV2PreviewResponseDocument.class),
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
                                            "title": "자바 기초",
                                            "description": "자바의 기본 문법을 학습합니다."
                                          }
                                        ],
                                        "keyPoints": ["DI", "AOP"]
                                      }
                                    }
                                    """)
                    )),
            @ApiResponse(responseCode = "404", description = "로드맵을 찾을 수 없음",
                    content = @Content(
                            schema = @Schema(implementation = com.process.clash.adapter.web.common.ApiResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "error": {
                                        "code": "SECTION_NOT_FOUND",
                                        "message": "로드맵을 찾을 수 없습니다.",
                                        "timestamp": "2025-01-02T10:00:00"
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetSectionV2PreviewDto.Response> getSectionPreview(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "로드맵 ID", example = "1") @PathVariable Long sectionId
    );
}
