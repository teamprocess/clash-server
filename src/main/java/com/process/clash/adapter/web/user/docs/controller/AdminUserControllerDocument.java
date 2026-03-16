package com.process.clash.adapter.web.user.docs.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "어드민 유저 API", description = "어드민 유저 관리")
public interface AdminUserControllerDocument {

    @Operation(summary = "강제 로그아웃", description = "특정 유저의 모든 세션 및 자동 로그인 정보를 만료시킵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "강제 로그아웃 성공",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "gildong123의 세션 및 자동 로그인 정보가 만료되었습니다."
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> forceLogout(
            @Parameter(description = "유저네임", example = "gildong123", required = true)
            @PathVariable String username
    );
}
