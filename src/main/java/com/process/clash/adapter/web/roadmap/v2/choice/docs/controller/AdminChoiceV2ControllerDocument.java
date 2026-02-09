package com.process.clash.adapter.web.roadmap.v2.choice.docs.controller;

import com.process.clash.adapter.web.roadmap.v2.choice.docs.request.CreateChoiceV2RequestDocument;
import com.process.clash.adapter.web.roadmap.v2.choice.docs.request.UpdateChoiceV2RequestDocument;
import com.process.clash.adapter.web.roadmap.v2.choice.docs.response.CreateChoiceV2ResponseDocument;
import com.process.clash.adapter.web.roadmap.v2.choice.docs.response.DeleteChoiceV2ResponseDocument;
import com.process.clash.adapter.web.roadmap.v2.choice.docs.response.UpdateChoiceV2ResponseDocument;
import com.process.clash.adapter.web.roadmap.v2.choice.dto.CreateChoiceV2Dto;
import com.process.clash.adapter.web.roadmap.v2.choice.dto.UpdateChoiceV2Dto;
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

@Tag(name = "선택지 V2 관리 API", description = "선택지 생성/수정/삭제 (V2)")
public interface AdminChoiceV2ControllerDocument {

    @Operation(summary = "선택지 생성", description = "신규 선택지를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공",
                    content = @Content(
                            schema = @Schema(implementation = CreateChoiceV2ResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "선택지 생성을 성공했습니다.",
                                      "data": {
                                        "choiceId": 1,
                                        "questionId": 1,
                                        "content": "객체지향",
                                        "isCorrect": true
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<CreateChoiceV2Dto.Response> createChoice(
            @Parameter(hidden = true) Actor actor,
            @RequestBody(description = "선택지 생성 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = CreateChoiceV2RequestDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "questionId": 1,
                                      "content": "객체지향",
                                      "isCorrect": true
                                    }
                                    """)
                    ))
            CreateChoiceV2Dto.Request request
    );

    @Operation(summary = "선택지 수정", description = "기존 선택지를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(
                            schema = @Schema(implementation = UpdateChoiceV2ResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "선택지 수정을 성공했습니다.",
                                      "data": {
                                        "choiceId": 1,
                                        "content": "객체지향 프로그래밍",
                                        "isCorrect": true
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<UpdateChoiceV2Dto.Response> updateChoice(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "선택지 ID", example = "1") @PathVariable Long choiceId,
            @RequestBody(description = "선택지 수정 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = UpdateChoiceV2RequestDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "content": "객체지향 프로그래밍",
                                      "isCorrect": true
                                    }
                                    """)
                    ))
            UpdateChoiceV2Dto.Request request
    );

    @Operation(summary = "선택지 삭제", description = "선택지를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공",
                    content = @Content(
                            schema = @Schema(implementation = DeleteChoiceV2ResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "선택지 삭제를 성공했습니다."
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> deleteChoice(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "선택지 ID", example = "1") @PathVariable Long choiceId
    );
}
