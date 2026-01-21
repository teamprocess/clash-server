package com.process.clash.adapter.web.compete.rival.battle.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessMessageResponseDoc;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "전체 배틀 정보 조회 응답")
public class FindAllBattleInfoResponseDoc extends SuccessMessageResponseDoc {

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
        public Long battleId;

        @Schema(description = "라이벌 이름", example = "이몽룡")
        public String rivalName;

        @Schema(description = "라이벌 사용자명", example = "mongryong")
        public String rivalUsername;

        @Schema(description = "라이벌 프로필 이미지 URL", example = "https://cdn.example.com/profile/2.png")
        public String rivalProfileImage;

        @Schema(description = "배틀 상태", example = "ONGOING")
        public String status;

        @Schema(description = "배틀 종료일", example = "2026-01-29")
        public String expireDate;
    }
}