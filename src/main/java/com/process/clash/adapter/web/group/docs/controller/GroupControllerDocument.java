package com.process.clash.adapter.web.group.docs.controller;

import com.process.clash.adapter.web.common.docs.response.SuccessMessageResponseDoc;
import com.process.clash.adapter.web.group.docs.request.CreateGroupRequestDoc;
import com.process.clash.adapter.web.group.docs.request.JoinGroupRequestDoc;
import com.process.clash.adapter.web.group.docs.request.UpdateGroupRequestDoc;
import com.process.clash.adapter.web.group.docs.response.GetAllGroupsResponseDoc;
import com.process.clash.adapter.web.group.docs.response.GetGroupDetailResponseDoc;
import com.process.clash.adapter.web.group.docs.response.GetGroupActivityResponseDoc;
import com.process.clash.adapter.web.group.docs.response.GetMyGroupsResponseDoc;
import com.process.clash.adapter.web.group.dto.CreateGroupDto;
import com.process.clash.adapter.web.group.dto.GetAllGroupsDto;
import com.process.clash.adapter.web.group.dto.GetGroupActivityDto;
import com.process.clash.adapter.web.group.dto.GetGroupDetailDto;
import com.process.clash.adapter.web.group.dto.GetMyGroupsDto;
import com.process.clash.adapter.web.group.dto.JoinGroupDto;
import com.process.clash.adapter.web.group.dto.UpdateGroupDto;
import com.process.clash.application.common.actor.Actor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "그룹 API", description = "그룹 생성/참여/활동 조회")
public interface GroupControllerDocument {

    @Operation(summary = "그룹 목록 조회", description = "전체 그룹 목록을 조회합니다.")
    @Parameters({
        @Parameter(name = "page", description = "페이지 번호 (1부터)", example = "1")
    })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(
                schema = @Schema(implementation = GetAllGroupsResponseDoc.class),
                examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "message": "그룹 목록 조회를 성공했습니다.",
                      "data": {
                        "pagination": {
                          "currentPage": 1,
                          "totalPages": 3,
                          "totalItems": 25,
                          "pageSize": 10,
                          "hasNext": true,
                          "hasPrevious": false
                        },
                        "groups": [
                          {
                            "id": 10,
                            "name": "알고리즘 스터디",
                            "description": "주 2회 문제 풀이",
                            "maxMembers": 10,
                            "currentMemberCount": 6,
                            "category": "CLASS",
                            "passwordRequired": false,
                            "owner": {
                              "id": 1,
                              "name": "홍길동"
                            },
                            "isMember": true
                          }
                        ]
                      }
                    }
                    """)
            ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetAllGroupsDto.Response> getAllGroups(
        @Parameter(hidden = true) Actor actor,
        @ParameterObject GetAllGroupsDto.Request request
    );

    @Operation(summary = "내 그룹 목록 조회", description = "내가 참여한 그룹 목록을 조회합니다.")
    @Parameters({
        @Parameter(name = "page", description = "페이지 번호 (1부터)", example = "1")
    })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = GetMyGroupsResponseDoc.class)))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetMyGroupsDto.Response> getMyGroups(
        @Parameter(hidden = true) Actor actor,
        @ParameterObject GetMyGroupsDto.Request request
    );

    @Operation(summary = "그룹 상세 조회", description = "그룹 상세 정보를 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = GetGroupDetailResponseDoc.class)))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetGroupDetailDto.Response> getGroupDetail(
        @Parameter(hidden = true) Actor actor,
        @Parameter(description = "그룹 ID", example = "1") @PathVariable Long groupId
    );

    @Operation(summary = "그룹 생성", description = "그룹을 생성합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "생성 성공",
            content = @Content(schema = @Schema(implementation = SuccessMessageResponseDoc.class)))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> createGroup(
        @Parameter(hidden = true) Actor actor,
        @RequestBody(
            required = true,
            description = "그룹 생성 요청",
            content = @Content(
                schema = @Schema(implementation = CreateGroupRequestDoc.class),
                examples = @ExampleObject(value = """
                    {
                      "name": "알고리즘 스터디",
                      "description": "주 2회 문제 풀이",
                      "maxMembers": 10,
                      "category": "CLASS",
                      "passwordRequired": false,
                      "password": "1234"
                    }
                    """)
            )
        ) CreateGroupDto.Request request
    );

    @Operation(summary = "그룹 수정", description = "그룹 정보를 수정합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정 성공",
            content = @Content(schema = @Schema(implementation = SuccessMessageResponseDoc.class)))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> updateGroup(
        @Parameter(hidden = true) Actor actor,
        @Parameter(description = "그룹 ID", example = "1") @PathVariable Long groupId,
        @RequestBody(
            required = true,
            description = "그룹 수정 요청",
            content = @Content(
                schema = @Schema(implementation = UpdateGroupRequestDoc.class),
                examples = @ExampleObject(value = """
                    {
                      "name": "알고리즘 스터디",
                      "description": "주 3회 문제 풀이",
                      "maxMembers": 12,
                      "category": "CLASS",
                      "passwordRequired": false,
                      "password": ""
                    }
                    """)
            )
        ) UpdateGroupDto.Request request
    );

    @Operation(summary = "그룹 삭제", description = "그룹을 삭제합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공",
            content = @Content(schema = @Schema(implementation = SuccessMessageResponseDoc.class)))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> deleteGroup(
        @Parameter(hidden = true) Actor actor,
        @Parameter(description = "그룹 ID", example = "1") @PathVariable Long groupId
    );

    @Operation(summary = "그룹 참여", description = "그룹에 참여합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "참여 성공",
            content = @Content(schema = @Schema(implementation = SuccessMessageResponseDoc.class)))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> joinGroup(
        @Parameter(hidden = true) Actor actor,
        @Parameter(description = "그룹 ID", example = "1") @PathVariable Long groupId,
        @RequestBody(
            required = false,
            description = "그룹 참여 요청",
            content = @Content(
                schema = @Schema(implementation = JoinGroupRequestDoc.class),
                examples = @ExampleObject(value = """
                    {
                      "password": "1234"
                    }
                    """)
            )
        ) JoinGroupDto.Request request
    );

    @Operation(summary = "그룹 탈퇴", description = "그룹을 탈퇴합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "탈퇴 성공",
            content = @Content(schema = @Schema(implementation = SuccessMessageResponseDoc.class)))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> quitGroup(
        @Parameter(hidden = true) Actor actor,
        @Parameter(description = "그룹 ID", example = "1") @PathVariable Long groupId
    );

    @Operation(summary = "그룹 활동 조회", description = "그룹원의 학습 현황을 조회합니다.")
    @Parameters({
        @Parameter(name = "page", description = "페이지 번호 (1부터)", example = "1")
    })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = GetGroupActivityResponseDoc.class)))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetGroupActivityDto.Response> getGroupActivity(
        @Parameter(hidden = true) Actor actor,
        @Parameter(description = "그룹 ID", example = "1") @PathVariable Long groupId,
        @ParameterObject GetGroupActivityDto.Request request
    );
}
