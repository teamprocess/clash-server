package com.process.clash.adapter.web.auth.docs.controller;

import com.process.clash.adapter.web.auth.docs.response.TestAdminResponseDoc;
import com.process.clash.adapter.web.auth.docs.response.TestAuthResponseDoc;
import com.process.clash.application.common.actor.Actor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "테스트 API", description = "인증/관리자 테스트")
public interface TestControllerDocument {

    @Operation(summary = "인증 테스트", description = "인증이 필요한 테스트 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증 테스트 성공",
                    content = @Content(
                            schema = @Schema(implementation = TestAuthResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "인증 테스트 성공"
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> testApi(
            @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "관리자 테스트", description = "관리자 권한이 필요한 테스트 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "관리자 테스트 성공",
                    content = @Content(
                            schema = @Schema(implementation = TestAdminResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "관리자 테스트 성공"
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> testAdmin(
            @Parameter(hidden = true) Actor actor
    );
}
