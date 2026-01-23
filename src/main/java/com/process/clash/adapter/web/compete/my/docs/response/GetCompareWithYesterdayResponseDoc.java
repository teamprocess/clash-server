package com.process.clash.adapter.web.compete.my.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "어제와 비교 응답")
public class GetCompareWithYesterdayResponseDoc extends SuccessResponseDoc {

    @Schema(description = "성공 여부", example = "true")
    public Boolean success;

    @Schema(description = "응답 메시지", example = "어제와의 비교 정보를 성공적으로 반환했습니다.")
    public String message;

    @Schema(description = "응답 데이터")
    public DataDoc data;

    public static class DataDoc {
        @Schema(description = "활동 시간 비교")
        public ActiveTimeDoc activeTime;

        @Schema(description = "기여자 수 비교")
        public ContributorsDoc contributors;
    }

    public static class ActiveTimeDoc {
        @Schema(description = "어제 활동 시간 (분)", example = "120")
        public Long yesterdayActiveTime;

        @Schema(description = "오늘 활동 시간 (분)", example = "150")
        public Long todayActiveTime;
    }

    public static class ContributorsDoc {
        @Schema(description = "어제 기여자 수", example = "3")
        public Integer yesterdayContributors;

        @Schema(description = "오늘 기여자 수", example = "5")
        public Integer todayContributors;
    }
}