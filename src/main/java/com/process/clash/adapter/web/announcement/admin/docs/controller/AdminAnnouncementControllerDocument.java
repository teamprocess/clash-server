package com.process.clash.adapter.web.announcement.admin.docs.controller;

import com.process.clash.adapter.web.announcement.admin.docs.response.CreateAnnouncementAdminResponseDocument;
import com.process.clash.adapter.web.announcement.admin.docs.response.GetAllAnnouncementsAdminResponseDocument;
import com.process.clash.adapter.web.announcement.admin.dto.CreateAnnouncementAdminDto;
import com.process.clash.adapter.web.announcement.admin.dto.GetAllAnnouncementsAdminDto;
import com.process.clash.adapter.web.announcement.admin.dto.UpdateAnnouncementAdminDto;
import com.process.clash.adapter.web.announcement.admin.dto.CreateAnnouncementAdminDto;
import com.process.clash.adapter.web.announcement.admin.dto.GetAllAnnouncementsAdminDto;
import com.process.clash.adapter.web.announcement.admin.dto.UpdateAnnouncementAdminDto;
import com.process.clash.adapter.web.common.docs.response.SuccessMessageResponseDocument;
import com.process.clash.application.common.actor.Actor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "어드민 공지 API", description = "공지 생성, 조회, 수정, 삭제")
public interface AdminAnnouncementControllerDocument {

    @Operation(summary = "공지 생성", description = "새로운 공지를 생성합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "생성 성공",
                    content = @Content(
                            schema = @Schema(implementation = CreateAnnouncementAdminResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "공지가 성공적으로 생성되었습니다.",
                                      "data": {
                                        "id": 1,
                                        "title": "서버 점검 안내",
                                        "author": "관리자",
                                        "userId": 1,
                                        "content": "3월 30일 오전 2시~4시 서버 점검이 예정되어 있습니다.",
                                        "startedAt": "2026-03-29T00:00:00Z",
                                        "endedAt": "2026-04-01T00:00:00Z",
                                        "createdAt": "2026-03-28T12:00:00Z"
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<CreateAnnouncementAdminDto.Response> createAnnouncement(
            @Parameter(hidden = true) Actor actor,
            CreateAnnouncementAdminDto.Request request
    );

    @Operation(summary = "전체 공지 조회", description = "모든 공지를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetAllAnnouncementsAdminResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "공지 목록을 성공적으로 조회했습니다.",
                                      "data": {
                                        "announcements": [
                                          {
                                            "id": 1,
                                            "title": "서버 점검 안내",
                                            "author": "관리자",
                                            "userId": 1,
                                            "content": "3월 30일 오전 2시~4시 서버 점검이 예정되어 있습니다.",
                                            "startedAt": "2026-03-29T00:00:00Z",
                                            "endedAt": "2026-04-01T00:00:00Z",
                                            "createdAt": "2026-03-28T12:00:00Z"
                                          }
                                        ]
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetAllAnnouncementsAdminDto.Response> getAllAnnouncements(
            @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "공지 수정", description = "특정 공지를 수정합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(
                            schema = @Schema(implementation = CreateAnnouncementAdminResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "공지가 성공적으로 수정되었습니다.",
                                      "data": {
                                        "id": 1,
                                        "title": "서버 점검 안내 (수정)",
                                        "author": "관리자",
                                        "userId": 1,
                                        "content": "점검 시간이 변경되었습니다.",
                                        "startedAt": "2026-03-29T00:00:00Z",
                                        "endedAt": "2026-04-02T00:00:00Z",
                                        "updatedAt": "2026-03-29T09:00:00Z"
                                      }
                                    }
                                    """)
                    )),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "공지를 찾을 수 없음",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "message": "공지를 찾을 수 없습니다."
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<UpdateAnnouncementAdminDto.Response> updateAnnouncement(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "수정할 공지 ID", example = "1", required = true) @PathVariable Long id,
            UpdateAnnouncementAdminDto.Request request
    );

    @Operation(summary = "공지 삭제", description = "특정 공지를 삭제합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "삭제 성공",
                    content = @Content(
                            schema = @Schema(implementation = SuccessMessageResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "공지가 성공적으로 삭제되었습니다."
                                    }
                                    """)
                    )),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "공지를 찾을 수 없음",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "message": "공지를 찾을 수 없습니다."
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> deleteAnnouncement(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "삭제할 공지 ID", example = "1", required = true) @PathVariable Long id
    );
}
