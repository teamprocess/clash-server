package com.process.clash.adapter.web.user.docs.controller;

import com.process.clash.adapter.web.user.docs.response.GetMyProfileResponseDoc;
import com.process.clash.adapter.web.user.docs.response.GetMyGitHubActivityResponseDoc;
import com.process.clash.adapter.web.user.dto.GetMyGitHubActivityDto;
import com.process.clash.adapter.web.user.dto.GetMyProfileDto;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.common.enums.PeriodCategory;
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
                            schema = @Schema(implementation = GetMyProfileResponseDoc.class)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetMyProfileDto.Response> getMyProfile(
            @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "특정 기간의 GitHub 활동 조회", description = "기간별 GitHub 활동을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetMyGitHubActivityResponseDoc.class)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetMyGitHubActivityDto.Response> getMyGitHubActivity(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "기간 (YEAR, MONTH, WEEK)", required = true) PeriodCategory period
    );
}
