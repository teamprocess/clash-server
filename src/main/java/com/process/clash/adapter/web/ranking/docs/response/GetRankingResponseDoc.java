package com.process.clash.adapter.web.ranking.docs.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "GetRankingResponse", description = "랭킹 조회 응답")
public class GetRankingResponseDoc {

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
    }
}