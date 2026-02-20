package com.process.clash.adapter.web.compete.my.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "내 활동 분석 응답")
public class AnalyzeMyActivityResponseDocument extends SuccessResponseDocument {

    @Schema(description = "성공 여부", example = "true")
    public Boolean success;

    @Schema(description = "응답 메시지", example = "내 활동 분석 결과를 성공적으로 반환했습니다.")
    public String message;

    @Schema(description = "응답 데이터")
    public DataDoc data;

    public static class DataDoc {
        @Schema(description = "카테고리", example = "GITHUB")
        public String category;

        @Schema(description = "연속 기록 목록")
        public List<StreakDoc> streaks;

        @Schema(description = "변동성 목록")
        public List<VariationDoc> variations;
    }

    public static class StreakDoc {
        @Schema(description = "날짜", example = "2025-01-01")
        public LocalDate date;

        @Schema(description = "상세 정보 (활동 시간 등)", example = "120")
        public Integer detailedInfo;

        @Schema(description = "잔디 색상 비율 (0~100, 상위/하위 15% 제외한 70% 트리밍 평균 기준 50% 매핑)", example = "50")
        public Integer colorRatio;
    }

    public static class VariationDoc {
        @Schema(description = "월", example = "1")
        public Integer month;

        @Schema(description = "월별 총합 변동성", example = "3600.0")
        public Double totalVariationPerMonth;
    }
}