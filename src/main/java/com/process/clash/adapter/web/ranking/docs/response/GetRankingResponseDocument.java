package com.process.clash.adapter.web.ranking.docs.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "GetRankingResponse", description = "랭킹 조회 응답")
public class GetRankingResponseDocument {

    @Schema(description = "성공 여부", example = "true")
    private Boolean success;

    @Schema(description = "응답 메시지", example = "랭킹 정보를 성공적으로 반환했습니다.")
    private String message;

    @Schema(description = "응답 데이터")
    private Data data;

    @Schema(name = "GetRankingResponseData", description = "랭킹 데이터")
    public static class Data {

        @Schema(description = "카테고리", example = "SERVER")
        private String category;

        @Schema(description = "기간", example = "WEEK")
        private String period;

        @Schema(description = "랭킹 목록")
        private List<UserRankingDoc> rankings;
    }

    @Schema(name = "UserRanking", description = "사용자 랭킹 정보")
    public static class UserRankingDoc {

        @Schema(description = "사용자 이름", example = "홍길동")
        private String name;

        @Schema(description = "프로필 이미지 URL", example = "https://cdn.example.com/profile/1.png")
        private String profileImage;

        @Schema(description = "라이벌 여부", example = "true")
        private Boolean isRival;

        @Schema(description = "연동된 ID", example = "github")
        private String linkedId;

        @Schema(description = "포인트", example = "1200")
        private Long point;

        @Schema(description = "장착 아이템")
        private EquippedItemsDoc equippedItems;
    }

    @Schema(name = "EquippedItems", description = "장착 아이템 정보")
    public static class EquippedItemsDoc {

        @Schema(description = "인시그니아")
        private EquippedItemDoc insigma;

        @Schema(description = "네임플레이트")
        private EquippedItemDoc nameplate;

        @Schema(description = "배너")
        private EquippedItemDoc banner;
    }

    @Schema(name = "EquippedItem", description = "장착 아이템 요약")
    public static class EquippedItemDoc {

        @Schema(description = "아이템 ID", example = "1")
        private Long id;

        @Schema(description = "아이템 이름", example = "기본 인시그니아")
        private String name;

        @Schema(description = "아이템 이미지 URL", example = "https://cdn.example.com/items/insignia.png")
        private String image;
    }
}
