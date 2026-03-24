package com.process.clash.adapter.web.compete.my.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "나의 기간별 활동 분석 응답")
public class AnalyzeMyPeriodResponseDocument extends SuccessResponseDocument {

    @Schema(description = "응답 데이터")
    public DataDoc data;

    public static class DataDoc {
        @Schema(description = "카테고리", example = "GITHUB")
        public String category;

        @Schema(description = "기간", example = "DAY")
        public String period;

        @Schema(description = "데이터 포인트 목록")
        public List<DataPointDoc> dataPoints;
    }

    public static class DataPointDoc {
        @Schema(description = "날짜 (MM-dd)", example = "03-18")
        public String date;

        @Schema(description = "값", example = "100.0")
        public Double point;
    }
}