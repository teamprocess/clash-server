package com.process.clash.adapter.web.announcement.admin.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "공지 생성 응답 (어드민)")
public class CreateAnnouncementAdminResponseDocument extends SuccessResponseDocument {

    @Schema(description = "응답 데이터")
    public DataDoc data;

    public static class DataDoc {
        @Schema(description = "공지 ID", example = "1")
        public Long id;

        @Schema(description = "공지 제목", example = "서버 점검 안내")
        public String title;

        @Schema(description = "작성자 이름", example = "관리자")
        public String author;

        @Schema(description = "작성자 유저 ID", example = "1", nullable = true)
        public Long userId;

        @Schema(description = "공지 내용", example = "3월 30일 오전 2시~4시 서버 점검이 예정되어 있습니다.")
        public String content;

        @Schema(description = "공지 시작 시각 (ISO 8601)", example = "2026-03-29T00:00:00Z")
        public String startedAt;

        @Schema(description = "공지 종료 시각 (ISO 8601)", example = "2026-04-01T00:00:00Z")
        public String endedAt;

        @Schema(description = "생성 시각 (ISO 8601)", example = "2026-03-28T12:00:00Z")
        public String createdAt;
    }
}
