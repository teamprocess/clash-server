package com.process.clash.adapter.web.user.docs.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "GitHub 활동 상세 데이터")
public class GetMyGitHubActivityDetailDataDocument {

    @Schema(description = "날짜", example = "2025-01-01")
    public String date;

    @Schema(description = "기여 수", example = "4")
    public Integer contributionCount;

    @Schema(description = "기여 레벨 (0~4)", example = "1")
    public Integer contributionLevel;

    @Schema(description = "커밋 수", example = "2")
    public Integer commitsCount;

    @Schema(description = "이슈 수", example = "1")
    public Integer issuesCount;

    @Schema(description = "PR 수", example = "1")
    public Integer prCount;

    @Schema(description = "리뷰 수", example = "0")
    public Integer reviewsCount;

    @Schema(description = "추가 라인", example = "120")
    public Long additionLines;

    @Schema(description = "삭제 라인", example = "45")
    public Long deletionLines;

@Schema(description = "가장 많이 커밋한 저장소 이름", example = "process-clash/clash-server", nullable = true)
    public String topCommitRepo;
}
