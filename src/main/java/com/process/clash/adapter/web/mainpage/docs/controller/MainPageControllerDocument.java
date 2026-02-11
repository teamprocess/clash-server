package com.process.clash.adapter.web.mainpage.docs.controller;

import com.process.clash.adapter.web.mainpage.docs.response.GetUserProfileResponseDocument;
import com.process.clash.adapter.web.mainpage.dto.GetUserProfileDto;
import com.process.clash.application.common.actor.Actor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "메인 페이지 API", description = "메인 페이지 정보")
public interface MainPageControllerDocument {

    @Operation(summary = "유저 프로필 조회", description = "상단바에 노출되는 유저 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetUserProfileResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "유저 정보를 성공적으로 반환했습니다.",
                                      "data": {
                                        "id": 1,
                                        "name": "홍길동",
                                        "username": "process123",
                                        "profileImage": "https://cdn.example.com/profile/1.png"
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetUserProfileDto.Response> getUserProfile(
            @Parameter(hidden = true) Actor actor
    );
}
