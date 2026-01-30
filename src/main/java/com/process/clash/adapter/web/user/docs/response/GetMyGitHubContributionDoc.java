package com.process.clash.adapter.web.user.docs.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "GitHub 기여 데이터")
public class GetMyGitHubContributionDoc {

    @Schema(description = "날짜", example = "2025-01-01")
    public String date;

    @Schema(description = "기여 수", example = "3")
    public Integer contributionCount;

    @Schema(description = "기여 레벨 (0~4)", example = "1")
    public Integer contributionLevel;
}
