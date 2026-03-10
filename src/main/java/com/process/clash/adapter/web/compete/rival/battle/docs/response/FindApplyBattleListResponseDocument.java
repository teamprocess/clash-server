package com.process.clash.adapter.web.compete.rival.battle.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessMessageResponseDocument;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "배틀 신청 목록 조회 응답")
public class FindApplyBattleListResponseDocument extends SuccessMessageResponseDocument {

    @Schema(description = "성공 여부", example = "true")
    public Boolean success;

    @Schema(description = "응답 메시지", example = "배틀 신청 목록을 성공적으로 조회했습니다.")
    public String message;

    @Schema(description = "응답 데이터")
    public DataDoc data;

    public static class DataDoc {
        @Schema(description = "배틀 신청 목록")
        public List<BattleApplyInfoDoc> battles;
    }

    public static class BattleApplyInfoDoc {
        @Schema(description = "배틀 ID", example = "1")
        public Long id;

        @Schema(description = "상대 정보")
        public EnemyDoc enemy;

        @Schema(description = "배틀 시작일", example = "2026-01-22")
        public LocalDate startDate;

        @Schema(description = "배틀 종료일", example = "2026-01-29")
        public LocalDate endDate;

        @Schema(description = "내가 신청한 배틀 여부", example = "true")
        public Boolean isMine;
    }

    public static class EnemyDoc {
        @Schema(description = "상대방 ID", example = "3")
        public Long id;

        @Schema(description = "사용자명", example = "이몽룡")
        public String name;

        @Schema(description = "프로필 이미지 URL", example = "https://cdn.example.com/profile/2.png")
        public String profileImage;
    }
}