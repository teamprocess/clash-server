package com.process.clash.adapter.web.compete.rival.battle.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessMessageResponseDoc;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "배틀 상세 정보 조회 응답")
public class FindDetailedBattleInfoResponseDoc extends SuccessMessageResponseDoc {

    @Schema(description = "성공 여부", example = "true")
    public Boolean success;

    @Schema(description = "응답 메시지", example = "라이벌과의 배틀 상세 정보를 성공적으로 반환했습니다.")
    public String message;

    @Schema(description = "응답 데이터")
    public DataDoc data;

    public static class DataDoc {
        @Schema(description = "배틀 ID", example = "1")
        public Long id;

        @Schema(description = "상대방 정보")
        public EnemyDoc enemy;

        @Schema(description = "배틀 종료일", example = "2026-01-29")
        public String expireDate;

        @Schema(description = "내 전체 승률 (%)", example = "65.5")
        public Double myOverallPercentage;
    }

    public static class EnemyDoc {
        @Schema(description = "상대방 ID", example = "3")
        public Long id;

        @Schema(description = "이름", example = "이몽룡")
        public String name;

        @Schema(description = "사용자명", example = "mongryong")
        public String username;

        @Schema(description = "프로필 이미지 URL", example = "https://cdn.example.com/profile/2.png")
        public String profileImage;
    }
}