package com.process.clash.adapter.web.compete.my.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "내 성장도 분석 응답")
public class GetMyGrowthRateResponseDoc extends SuccessResponseDoc {

    @Schema(description = "성공 여부", example = "true")
    public Boolean success;

    @Schema(description = "응답 메시지", example = "성장도 분석 결과를 성공적으로 반환했습니다.")
    public String message;

    @Schema(description = "응답 데이터")
    public DataDoc data;

    public static class DataDoc {
        @Schema(description = "성장도 데이터 포인트 목록")
        public List<DataPointDoc> dataPoint;
    }

    public static class DataPointDoc {
        @Schema(description = "날짜", example = "2025-01-01")
        public LocalDate date;

        @Schema(description = "성장률", example = "15.5")
        public Double rate;
    }
}