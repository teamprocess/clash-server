package com.process.clash.adapter.web.user.docs.controller;

import com.process.clash.adapter.web.common.docs.response.SuccessMessageResponseDocument;
import com.process.clash.adapter.web.user.docs.response.GetMyUserNoticesResponseDocument;
import com.process.clash.adapter.web.user.dto.GetMyUserNoticesDto;
import com.process.clash.application.common.actor.Actor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "알림 API", description = "사용자 알림 조회 및 읽음 처리")
public interface UserNoticeControllerDocument {

    @Operation(summary = "내 알림 목록 조회", description = "나에게 수신된 알림 목록을 최신순으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetMyUserNoticesResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "알림 목록을 성공적으로 조회했습니다.",
                                      "data": {
                                        "notices": [
                                          {
                                            "id": 1,
                                            "category": "APPLY_RIVAL",
                                            "message": "상대방이 라이벌을 신청했어요.",
                                            "requiresAction": true,
                                            "isRead": false,
                                            "senderId": 42,
                                            "senderName": "김철수",
                                            "senderUsername": "chulsoo_kim",
                                            "senderProfileImage": "https://example.com/profiles/sender.jpg",
                                            "receiverId": 1,
                                            "receiverName": "이영희",
                                            "receiverUsername": "younghee_lee",
                                            "receiverProfileImage": null,
                                            "createdAt": "2026-02-21T10:00:00Z"
                                          }
                                        ]
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetMyUserNoticesDto.Response> getMyNotices(
            @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "알림 읽음 처리", description = "특정 알림을 읽음 처리합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "읽음 처리 성공",
                    content = @Content(
                            schema = @Schema(implementation = SuccessMessageResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "알림을 읽음 처리했습니다."
                                    }
                                    """)
                    )),
            @ApiResponse(responseCode = "404", description = "알림을 찾을 수 없음",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "message": "알림을 찾을 수 없습니다."
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> readNotice(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "읽음 처리할 알림 ID", example = "1", required = true)
            @PathVariable Long id
    );
}
