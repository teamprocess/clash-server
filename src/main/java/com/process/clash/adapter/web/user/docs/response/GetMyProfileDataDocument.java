package com.process.clash.adapter.web.user.docs.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "내 계정/프로필 데이터")
public class GetMyProfileDataDocument {

    @Schema(description = "사용자 ID", example = "1")
    public Long id;

    @Schema(description = "생성일시", example = "2025-01-02T09:00:00Z")
    public String createdAt;

    @Schema(description = "수정일시", example = "2025-01-05T12:30:00Z")
    public String updatedAt;

    @Schema(description = "아이디", example = "process123")
    public String username;

    @Schema(description = "이름", example = "홍길동")
    public String name;

    @Schema(description = "이메일", example = "hong@example.com")
    public String email;

    @Schema(description = "역할", example = "USER")
    public String role;

    @Schema(description = "프로필 이미지 URL", example = "https://cdn.example.com/profile/1.png")
    public String profileImage;

    @Schema(description = "총 경험치", example = "1200")
    public Integer totalExp;

    @Schema(description = "총 쿠키", example = "300")
    public Integer totalCookie;

    @Schema(description = "총 토큰 (폐기 예정 호환 필드)", example = "0")
    public Integer totalToken;

    @Schema(description = "전공", example = "SERVER")
    public String major;

    @Schema(description = "유저 상태", example = "ACTIVE")
    public String userStatus;

    @Schema(description = "GitHub 연동 여부", example = "true")
    public Boolean githubLinked;

    @Schema(description = "실시간 활동 상태", example = "ONLINE")
    public String activityStatus;

    @Schema(description = "현재 장착 아이템")
    public EquippedItemsDocument equippedItems;

    @Schema(description = "장착 아이템")
    public static class EquippedItemsDocument {
        @Schema(description = "인시그니아")
        public EquippedItemDocument insigma;

        @Schema(description = "네임플레이트")
        public EquippedItemDocument nameplate;

        @Schema(description = "배너")
        public EquippedItemDocument banner;
    }

    @Schema(description = "장착 아이템 요약")
    public static class EquippedItemDocument {
        @Schema(description = "아이템 ID", example = "1")
        public Long id;

        @Schema(description = "아이템 이름", example = "기본 인시그니아")
        public String name;

        @Schema(description = "아이템 이미지 URL", example = "https://cdn.example.com/items/insignia.png")
        public String image;
    }
}
