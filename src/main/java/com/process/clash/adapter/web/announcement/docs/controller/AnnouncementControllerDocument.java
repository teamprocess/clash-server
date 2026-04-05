package com.process.clash.adapter.web.announcement.docs.controller;

import com.process.clash.adapter.web.announcement.docs.response.GetActiveAnnouncementsResponseDocument;
import com.process.clash.adapter.web.announcement.dto.GetActiveAnnouncementsDto;
import com.process.clash.adapter.web.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "공지 API", description = "활성 공지 목록 조회")
public interface AnnouncementControllerDocument {

    @Operation(summary = "활성 공지 전체 조회", description = "현재 활성 상태인 모든 공지(startedAt <= now <= endedAt)를 startedAt 내림차순으로 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetActiveAnnouncementsResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "활성 공지 목록을 성공적으로 조회했습니다.",
                                      "data": {
                                        "announcements": [
                                          {
                                            "id": 1,
                                            "title": "서버 점검 안내",
                                            "author": "PROSSES",
                                            "userId": null,
                                            "content": "3월 30일 오전 2시~4시 서버 점검이 예정되어 있습니다.",
                                            "startedAt": "2026-03-29T09:00:00+09:00",
                                            "endedAt": "2026-04-01T09:00:00+09:00"
                                          }
                                        ]
                                      }
                                    }
                                    """)
                    ))
    })
    ApiResponse<GetActiveAnnouncementsDto.Response> getActiveAnnouncements();
}
