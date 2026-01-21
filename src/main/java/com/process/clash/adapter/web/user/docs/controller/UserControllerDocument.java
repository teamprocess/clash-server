package com.process.clash.adapter.web.user.docs.controller;

import com.process.clash.adapter.web.user.docs.response.GetMyProfileResponseDoc;
import com.process.clash.adapter.web.user.dto.GetMyProfileDto;
import com.process.clash.application.common.actor.Actor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "유저 API", description = "내 계정/프로필 정보")
public interface UserControllerDocument {

    @Operation(summary = "내 프로필 조회", description = "로그인한 사용자의 계정 및 프로필 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetMyProfileResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "내 프로필을 성공적으로 조회했습니다.",
                                      "data": {
                                        "id": 1,
                                        "createdAt": "2025-01-02T09:00:00Z",
                                        "updatedAt": "2025-01-05T12:30:00Z",
                                        "username": "process123",
                                        "name": "홍길동",
                                        "email": "hong@example.com",
                                        "role": "USER",
                                        "profileImage": "https://cdn.example.com/profile/1.png",
                                        "totalExp": 1200,
                                        "totalCookie": 300,
                                        "totalToken": 20,
                                        "major": "SERVER",
                                        "userStatus": "ACTIVE"
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetMyProfileDto.Response> getMyProfile(
            @Parameter(hidden = true) Actor actor
    );
}
