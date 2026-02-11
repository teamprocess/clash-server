package com.process.clash.adapter.web.roadmap.v2.chapter.docs.controller;

import com.process.clash.adapter.web.roadmap.v2.chapter.docs.request.CreateChapterV2RequestDocument;
import com.process.clash.adapter.web.roadmap.v2.chapter.docs.request.UpdateChapterV2RequestDocument;
import com.process.clash.adapter.web.roadmap.v2.chapter.docs.response.CreateChapterV2ResponseDocument;
import com.process.clash.adapter.web.roadmap.v2.chapter.docs.response.DeleteChapterV2ResponseDocument;
import com.process.clash.adapter.web.roadmap.v2.chapter.docs.response.UpdateChapterV2ResponseDocument;
import com.process.clash.adapter.web.roadmap.v2.chapter.dto.CreateChapterV2Dto;
import com.process.clash.adapter.web.roadmap.v2.chapter.dto.UpdateChapterV2Dto;
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

@Tag(name = "챕터 V2 관리 API", description = "챕터 생성/수정/삭제 (V2)")
public interface AdminChapterV2ControllerDocument {

    @Operation(summary = "챕터 생성", description = "신규 챕터를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공",
                    content = @Content(
                            schema = @Schema(implementation = CreateChapterV2ResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "챕터 생성을 성공했습니다.",
                                      "data": {
                                        "chapterId": 1,
                                        "sectionId": 10,
                                        "title": "자바 기초",
                                        "description": "자바의 기본 문법을 학습합니다.",
                                        "orderIndex": 0
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<CreateChapterV2Dto.Response> createChapter(
            @Parameter(hidden = true) Actor actor,
            @RequestBody(description = "챕터 생성 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = CreateChapterV2RequestDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "sectionId": 10,
                                      "title": "자바 기초",
                                      "description": "자바의 기본 문법을 학습합니다.",
                                      "orderIndex": 0
                                    }
                                    """)
                    ))
            CreateChapterV2Dto.Request request
    );

    @Operation(summary = "챕터 수정", description = "기존 챕터를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(
                            schema = @Schema(implementation = UpdateChapterV2ResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "챕터 수정을 성공했습니다.",
                                      "data": {
                                        "chapterId": 1,
                                        "title": "자바 심화",
                                        "description": "자바의 고급 문법을 학습합니다.",
                                        "orderIndex": 1
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<UpdateChapterV2Dto.Response> updateChapter(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "챕터 ID", example = "1") @PathVariable Long chapterId,
            @RequestBody(description = "챕터 수정 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = UpdateChapterV2RequestDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "title": "자바 심화",
                                      "description": "자바의 고급 문법을 학습합니다.",
                                      "orderIndex": 1
                                    }
                                    """)
                    ))
            UpdateChapterV2Dto.Request request
    );

    @Operation(summary = "챕터 삭제", description = "챕터를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공",
                    content = @Content(
                            schema = @Schema(implementation = DeleteChapterV2ResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "챕터 삭제를 성공했습니다."
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> deleteChapter(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "챕터 ID", example = "1") @PathVariable Long chapterId
    );
}
