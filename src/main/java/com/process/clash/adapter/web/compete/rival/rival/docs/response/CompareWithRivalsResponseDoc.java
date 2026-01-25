package com.process.clash.adapter.web.compete.rival.rival.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessMessageResponseDoc;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "라이벌과 비교 응답")
public class CompareWithRivalsResponseDoc extends SuccessMessageResponseDoc {

    @Schema(description = "성공 여부", example = "true")
    public Boolean success;

    @Schema(description = "응답 메시지", example = "라이벌과의 비교 정보를 성공적으로 반환했습니다.")
    public String message;

    @Schema(description = "응답 데이터")
    public DataDoc data;

    public static class DataDoc {
        @Schema(description = "비교 카테고리", example = "GITHUB")
        public String category;

        @Schema(description = "비교 기간", example = "WEEKLY")
        public String period;

        @Schema(description = "전체 데이터 (본인 + 라이벌)")
        public List<TotalDataDoc> totalData;
    }

    public static class TotalDataDoc {
        @Schema(description = "사용자 ID", example = "1")
        public Long id;

        @Schema(description = "이름", example = "홍길동")
        public String name;

        @Schema(description = "데이터 포인트 목록")
        public List<DataPointDoc> dataPoint;
    }

    public static class DataPointDoc {
        @Schema(description = "날짜", example = "2025-01-01")
        public LocalDate date;

        @Schema(description = "포인트", example = "150.5")
        public Double point;
    }
}