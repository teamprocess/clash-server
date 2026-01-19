package com.process.clash.adapter.web.roadmap.section.docs.controller;

import com.process.clash.adapter.web.roadmap.section.docs.request.CreateSectionRequestDoc;
import com.process.clash.adapter.web.roadmap.section.docs.request.UpdateSectionRequestDoc;
import com.process.clash.adapter.web.roadmap.section.docs.response.CreateSectionResponseDoc;
import com.process.clash.adapter.web.roadmap.section.docs.response.DeleteSectionResponseDoc;
import com.process.clash.adapter.web.roadmap.section.docs.response.UpdateSectionResponseDoc;
import com.process.clash.adapter.web.roadmap.section.dto.CreateSectionDto;
import com.process.clash.adapter.web.roadmap.section.dto.UpdateSectionDto;
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

@Tag(name = "로드맵 관리 API", description = "로드맵 생성/수정/삭제")
public interface AdminSectionControllerDocument {

    @Operation(summary = "로드맵 생성", description = "신규 로드맵을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공",
                    content = @Content(
                            schema = @Schema(implementation = CreateSectionResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "로드맵 생성을 성공했습니다.",
                                      "data": {
                                        "sectionId": 1,
                                        "major": "SERVER",
                                        "title": "스프링 입문",
                                        "category": "백엔드",
                                        "description": "스프링 핵심 개념을 학습합니다.",
                                        "keyPoints": [
                                          "DI",
                                          "AOP"
                                        ],
                                        "createdAt": "2025-01-01T12:00:00"
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<CreateSectionDto.Response> createSection(
            @Parameter(hidden = true) Actor actor,
            @RequestBody(description = "로드맵 생성 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = CreateSectionRequestDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "major": "SERVER",
                                      "title": "스프링 입문",
                                      "categoryId": 10,
                                      "description": "스프링 핵심 개념을 학습합니다.",
                                      "keyPoints": [
                                        "DI",
                                        "AOP"
                                      ]
                                    }
                                    """)
                    ))
            CreateSectionDto.Request request
    );

    @Operation(summary = "로드맵 수정", description = "기존 로드맵을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(
                            schema = @Schema(implementation = UpdateSectionResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "로드맵 수정을 성공했습니다.",
                                      "data": {
                                        "sectionId": 1,
                                        "title": "스프링 심화",
                                        "category": "백엔드",
                                        "description": "스프링 심화 개념을 학습합니다.",
                                        "keyPoints": [
                                          "DI",
                                          "AOP",
                                          "Security"
                                        ],
                                        "updatedAt": "2025-01-02T10:00:00"
                                      }
                                    }
                                    """)
                    )),
            @ApiResponse(responseCode = "422", description = "로드맵은 본인을 선행 로드맵으로 지정할 수 없습니다.",
                    content = @Content(
                            schema = @Schema(implementation = com.process.clash.adapter.web.common.ApiResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "error": {
                                        "code": "SECTION_CIRCULAR_DEPENDENCY",
                                        "message": "로드맵은 본인을 선행 로드맵으로 지정할 수 없습니다.",
                                        "timestamp": "2025-01-02T10:00:00"
                                      }
                                    }
                                    """)
                    )
            )
    })
    com.process.clash.adapter.web.common.ApiResponse<UpdateSectionDto.Response> updateSection(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "로드맵 ID", example = "1") @PathVariable Long sectionId,
            @RequestBody(description = "로드맵 수정 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = UpdateSectionRequestDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "title": "스프링 심화",
                                      "description": "스프링 심화 개념을 학습합니다.",
                                      "orderIndex": 2,
                                      "keyPoints": [
                                        "DI",
                                        "AOP",
                                        "Security"
                                      ],
                                      "prerequisiteSectionIds": [1]
                                    }
                                    """)
                    ))
            UpdateSectionDto.Request request
    );

    @Operation(summary = "로드맵 삭제", description = "로드맵을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공",
                    content = @Content(
                            schema = @Schema(implementation = DeleteSectionResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "로드맵 삭제를 성공했습니다. (연관된 챕터, 미션, 질문도 함께 삭제됩니다)"
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> deleteSection(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "로드맵 ID", example = "1") @PathVariable Long sectionId
    );
}
