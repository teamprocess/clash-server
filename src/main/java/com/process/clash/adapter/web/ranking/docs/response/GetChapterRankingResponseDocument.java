package com.process.clash.adapter.web.ranking.docs.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "GetChapterRankingResponse", description = "챕터 랭킹 조회 응답")
public class GetChapterRankingResponseDocument {

    @Schema(description = "성공 여부", example = "true")
    private Boolean success;

    @Schema(description = "응답 메시지", example = "챕터 완료 수 랭킹 조회를 성공했습니다.")
    private String message;

    @Schema(description = "응답 데이터")
    private Data data;

    @Schema(name = "GetChapterRankingResponseData", description = "챕터 랭킹 데이터")
    public static class Data {

        @Schema(description = "내 랭킹 정보")
        private MyRankingDoc myRank;

        @Schema(description = "전체 랭커 목록")
        private List<RankersDoc> allRankers;
    }

    @Schema(name = "MyRanking", description = "내 챕터 랭킹 정보")
    public static class MyRankingDoc {

        @Schema(description = "순위", example = "5")
        private Integer rank;

        @Schema(description = "완료한 챕터 수", example = "28")
        private Integer completedChaptersCount;

        @Schema(description = "사용자 ID", example = "100")
        private Long id;

        @Schema(description = "사용자 이름", example = "나")
        private String name;

        @Schema(description = "프로필 이미지 URL", example = "https://cdn.example.com/profile/me.png")
        private String profileImage;
    }

    @Schema(name = "Rankers", description = "랭커 정보")
    public static class RankersDoc {

        @Schema(description = "순위", example = "1")
        private Integer rank;

        @Schema(description = "완료한 챕터 수", example = "42")
        private Integer completedChaptersCount;

        @Schema(description = "사용자 ID", example = "1")
        private Long id;

        @Schema(description = "사용자 이름", example = "홍길동")
        private String name;

        @Schema(description = "프로필 이미지 URL", example = "https://cdn.example.com/profile/1.png")
        private String profileImage;
    }
}