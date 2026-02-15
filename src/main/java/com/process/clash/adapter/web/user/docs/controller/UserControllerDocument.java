package com.process.clash.adapter.web.user.docs.controller;

import com.process.clash.adapter.web.user.docs.response.GetMyProfileResponseDocument;
import com.process.clash.adapter.web.user.docs.response.GetMyGitHubActivityResponseDocument;
import com.process.clash.adapter.web.user.docs.response.GetMyGitHubActivityDetailResponseDocument;
import com.process.clash.adapter.web.user.docs.response.GetMyGitHubLinkStatusResponseDocument;
import com.process.clash.adapter.web.user.docs.response.GetMyItemsResponseDocument;
import com.process.clash.adapter.web.user.docs.response.GetMyActivityCalendarResponseDocument;
import com.process.clash.adapter.web.user.docs.response.IssueProfileImageUploadUrlResponseDocument;
import com.process.clash.adapter.web.user.docs.response.LinkGitHubOAuthResponseDocument;
import com.process.clash.adapter.web.user.dto.GetMyGitHubLinkStatusDto;
import com.process.clash.adapter.web.user.dto.GetMyGitHubActivityDto;
import com.process.clash.adapter.web.user.dto.GetMyGitHubActivityDetailDto;
import com.process.clash.adapter.web.user.dto.GetMyItemsDto;
import com.process.clash.adapter.web.user.dto.GetMyActivityCalendarDto;
import com.process.clash.adapter.web.user.dto.GetMyProfileDto;
import com.process.clash.adapter.web.user.dto.IssueProfileImageUploadUrlDto;
import com.process.clash.adapter.web.user.dto.LinkGitHubOAuthDto;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.common.enums.PeriodCategory;
import com.process.clash.domain.common.enums.UserItemCategory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.time.YearMonth;

@Tag(name = "유저 API", description = "내 계정/프로필 정보")
public interface UserControllerDocument {

    @Operation(summary = "내 프로필 조회", description = "로그인한 사용자의 계정 및 프로필 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetMyProfileResponseDocument.class)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetMyProfileDto.Response> getMyProfile(
            @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "특정 기간의 GitHub 활동 조회", description = "기간별 GitHub 활동을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetMyGitHubActivityResponseDocument.class)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetMyGitHubActivityDto.Response> getMyGitHubActivity(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "기간 (YEAR, MONTH, WEEK)", required = true) PeriodCategory period
    );

    @Operation(summary = "특정 날짜의 GitHub 활동 조회", description = "특정 날짜의 GitHub 활동을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetMyGitHubActivityDetailResponseDocument.class)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetMyGitHubActivityDetailDto.Response> getMyGitHubActivityDetail(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "조회 날짜 (yyyy-MM-dd)", required = true) LocalDate date
    );

    @Operation(summary = "GitHub OAuth 연동", description = "GitHub OAuth 인증 코드를 교환해 계정을 연동합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "연동 성공",
                    content = @Content(
                            schema = @Schema(implementation = LinkGitHubOAuthResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "GitHub 계정 연동을 완료했습니다.",
                                      "data": {
                                        "gitHubId": "octocat",
                                        "linked": true
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<LinkGitHubOAuthDto.Response> linkGitHubOAuth(
            @Parameter(hidden = true) Actor actor,
            @RequestBody(description = "GitHub OAuth 코드", required = true,
                    content = @Content(
                            schema = @Schema(implementation = LinkGitHubOAuthDto.Request.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "code": "github_oauth_code"
                                    }
                                    """)
                    ))
            LinkGitHubOAuthDto.Request request
    );

    @Operation(summary = "GitHub 연동 상태 조회", description = "현재 로그인한 사용자의 GitHub 연동 여부를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetMyGitHubLinkStatusResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "GitHub 연동 상태를 성공적으로 조회했습니다.",
                                      "data": {
                                        "linked": true,
                                        "gitHubId": "octocat"
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetMyGitHubLinkStatusDto.Response> getMyGitHubLinkStatus(
            @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "보유한 아이템 목록 조회", description = "보유한 아이템 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetMyItemsResponseDocument.class)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetMyItemsDto.Response> getMyItems(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "카테고리 (ALL, INSIGNIA, NAMEPLATE, BANNER)", required = true) UserItemCategory category
    );

    @Operation(summary = "활동 캘린더 조회", description = "특정 월의 활동 캘린더를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetMyActivityCalendarResponseDocument.class)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetMyActivityCalendarDto.Response> getMyActivityCalendar(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "조회 월 (yyyy-MM)", required = true) YearMonth month
    );

    @Operation(summary = "프로필 이미지 업로드 URL 발급", description = "S3에 직접 업로드할 수 있는 presigned PUT URL을 발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "발급 성공",
                    content = @Content(
                            schema = @Schema(implementation = IssueProfileImageUploadUrlResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "프로필 이미지 업로드 URL 발급을 성공했습니다.",
                                      "data": {
                                        "uploadUrl": "https://clash-profile.s3.ap-northeast-2.amazonaws.com/users/profile-images/user-1/9d2f5cf66b814f06ab7ebef4d98be8fc.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&...",
                                        "objectKey": "users/profile-images/user-1/9d2f5cf66b814f06ab7ebef4d98be8fc.png",
                                        "fileUrl": "https://clash-profile.s3.ap-northeast-2.amazonaws.com/users/profile-images/user-1/9d2f5cf66b814f06ab7ebef4d98be8fc.png",
                                        "httpMethod": "PUT",
                                        "contentType": "image/png",
                                        "expiresInSeconds": 300
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<IssueProfileImageUploadUrlDto.Response> issueProfileImageUploadUrl(
            @Parameter(hidden = true) Actor actor,
            @RequestBody(description = "Presigned URL 발급 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = IssueProfileImageUploadUrlDto.Request.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "fileName": "profile.png",
                                      "contentType": "image/png"
                                    }
                                    """)
                    ))
            IssueProfileImageUploadUrlDto.Request request
    );
}
