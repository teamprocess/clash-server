package com.process.clash.adapter.web.helpcontent.docs.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.helpcontent.dto.CreateHelpContentDto;
import com.process.clash.adapter.web.helpcontent.dto.UpdateHelpContentDto;
import com.process.clash.application.common.actor.Actor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "어드민 도움말 API", description = "클라이언트 도움말 수정")
public interface AdminHelpContentControllerDocument {

    @Operation(summary = "도움말 생성", description = "새 도움말 키와 전체 텍스트를 생성합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "생성 성공",
                    content = @Content(
                            schema = @Schema(implementation = CreateHelpContentDto.Response.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "도움말 내용이 성공적으로 생성되었습니다.",
                                      "data": {
                                        "key": "new-tooltip",
                                        "content": "새 안내 문구입니다.",
                                        "version": 1,
                                        "createdAt": "2026-07-14T14:00:00Z"
                                      }
                                    }
                                    """)
                    )),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 존재하는 도움말 키")
    })
    ApiResponse<CreateHelpContentDto.Response> createHelpContent(
            @Parameter(hidden = true) Actor actor,
            CreateHelpContentDto.Request request
    );

    @Operation(summary = "도움말 수정", description = "도움말 전체 텍스트를 수정하고 ETag 버전을 증가시킵니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(
                            schema = @Schema(implementation = UpdateHelpContentDto.Response.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "도움말 내용이 성공적으로 수정되었습니다.",
                                      "data": {
                                        "key": "cookie-tooltip",
                                        "content": "쿠키는 상점에서 사용 할 수 있어요!",
                                        "version": 2,
                                        "updatedAt": "2026-07-14T14:00:00Z"
                                      }
                                    }
                                    """)
                    )),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "도움말을 찾을 수 없음")
    })
    ApiResponse<UpdateHelpContentDto.Response> updateHelpContent(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "수정할 도움말 키", example = "cookie-tooltip", required = true) @PathVariable String key,
            UpdateHelpContentDto.Request request
    );
}
