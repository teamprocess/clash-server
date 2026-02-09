package com.process.clash.adapter.web.compete.rival.battle.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessMessageResponseDocument;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "배틀 정보 분석 응답")
public class AnalyzeBattleInfoResponseDocument extends SuccessMessageResponseDocument {

    @Schema(description = "성공 여부", example = "true")
    public Boolean success;

    @Schema(description = "응답 메시지", example = "라이벌과의 배틀 정보 분석을 성공적으로 반환했습니다.")
    public String message;

    @Schema(description = "응답 데이터")
    public DataDoc data;

    public static class DataDoc {
        @Schema(description = "분석 카테고리", example = "CLASH")
        public String category;

        @Schema(description = "배틀 ID", example = "1")
        public Long id;

        @Schema(description = "상대방 포인트", example = "850")
        public Integer enemyPoint;

        @Schema(description = "내 포인트", example = "720")
        public Integer myPoint;
    }
}