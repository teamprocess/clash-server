package com.process.clash.adapter.web.realtime.docs.controller;

import com.process.clash.adapter.web.realtime.dto.IssueSocketTokenDto;
import com.process.clash.application.common.actor.Actor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "소켓 토큰 API", description = "실시간 소켓 연결 토큰 발급")
public interface SocketTokenControllerDocument {

    @Operation(summary = "소켓 연결 토큰 발급", description = "실시간 소켓 연결을 위한 단기 토큰을 발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토큰 발급 성공",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "소켓 연결 토큰 발급을 성공했습니다.",
                                      "data": {
                                        "token": "eyJhbGciOiJIUzI1NiJ9...",
                                        "expiresInSeconds": 300
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<IssueSocketTokenDto.Response> issueToken(
            @Parameter(hidden = true) Actor actor
    );
}
