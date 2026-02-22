package com.process.clash.adapter.web.user.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "내 알림 목록 조회 응답")
public class GetMyUserNoticesResponseDocument extends SuccessResponseDocument {

    @Schema(description = "응답 데이터")
    public DataDoc data;

    public static class DataDoc {
        @Schema(description = "알림 목록")
        public List<NoticeItemDoc> notices;
    }

    public static class NoticeItemDoc {
        @Schema(description = "알림 ID", example = "1")
        public Long id;

        @Schema(description = "알림 카테고리", example = "APPLY_RIVAL")
        public String category;

        @Schema(description = "알림 메시지", example = "상대방이 라이벌을 신청했어요.")
        public String message;

        @Schema(description = "액션 필요 여부", example = "true")
        public Boolean requiresAction;

        @Schema(description = "읽음 여부", example = "false")
        public Boolean isRead;

        @Schema(description = "발신자 ID", example = "42")
        public Long senderId;

        @Schema(description = "발신자 이름", example = "김철수")
        public String senderName;

        @Schema(description = "발신자 유저네임", example = "chulsoo_kim")
        public String senderUsername;

        @Schema(description = "발신자 프로필 이미지 URL", example = "https://example.com/profiles/sender.jpg", nullable = true)
        public String senderProfileImage;

        @Schema(description = "수신자 ID", example = "1")
        public Long receiverId;

        @Schema(description = "수신자 이름", example = "이영희")
        public String receiverName;

        @Schema(description = "수신자 유저네임", example = "younghee_lee")
        public String receiverUsername;

        @Schema(description = "수신자 프로필 이미지 URL", example = "https://example.com/profiles/receiver.jpg", nullable = true)
        public String receiverProfileImage;

        @Schema(description = "생성 시각", example = "2026-02-21T10:00:00Z")
        public String createdAt;
    }
}
