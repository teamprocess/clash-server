package com.process.clash.adapter.web.compete.rival.battle.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessMessageResponseDocument;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "전체 배틀 정보 조회 응답")
public class FindAllBattleInfoResponseDocument extends SuccessMessageResponseDocument {

    @Schema(description = "성공 여부", example = "true")
    public Boolean success;

    @Schema(description = "응답 메시지", example = "라이벌과의 배틀 정보를 성공적으로 반환했습니다.")
    public String message;

    @Schema(description = "응답 데이터")
    public DataDoc data;

    public static class DataDoc {
        @Schema(description = "배틀 목록")
        public List<BattleInfoDoc> battles;
    }

    public static class BattleInfoDoc {
        @Schema(description = "배틀 ID", example = "1")
        public Long id;

        @Schema(description = "상대 정보")
        public EnemyDoc enemy;

        @Schema(description = "배틀 종료일", example = "2026-01-29")
        public LocalDate expireDate;

        @Schema(description = "결과", example = "WON")
        public String result; // WON, LOST, WINNING, LOSING, DRAWING, DRAWN, PENDING
    }

    public static class EnemyDoc {
        @Schema(description = "상대방 ID", example = "3")
        public Long id;

        @Schema(description = "사용자명", example = "mongryong")
        public String name;

        @Schema(description = "프로필 이미지 URL", example = "https://cdn.example.com/profile/2.png")
        public String profileImage;
    }
}