package com.process.clash.adapter.web.compete.my.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.time.LocalDate;

@Schema(description = "깃허브 비교 응답")
public class CompareGitHubResponseDocument extends SuccessResponseDocument {

    @Schema(description = "성공 여부", example = "true")
    public Boolean success;

    @Schema(description = "응답 메시지", example = "어제와 비교한 유저의 깃허브 정보 조회를 성공적으로 완료 했습니다.")
    public String message;

    @Schema(description = "응답 데이터")
    public DataDoc data;

    public static class DataDoc {
        @Schema(description = "어제 GitHub 활동")
        public OneDayStatsDoc yesterday;

        @Schema(description = "오늘 GitHub 활동")
        public OneDayStatsDoc today;
    }

    public static class OneDayStatsDoc {
        @Schema(description = "날짜", example = "2025-01-01")
        public LocalDate date;

        @Schema(description = "커밋 정보")
        public CommitDoc commit;

        @Schema(description = "Pull Request 정보")
        public PullRequestDoc pullRequest;

        @Schema(description = "이슈 정보")
        public IssueDoc issue;

        @Schema(description = "리뷰 정보")
        public ReviewDoc review;
    }

    public static class CommitDoc {
        @Schema(description = "커밋 수", example = "4")
        public Integer count;

        @Schema(description = "대표 레포지토리", example = "clash-server")
        public String representationRepo;

        @Schema(description = "추가된 라인 수", example = "120")
        public Long addLines;

        @Schema(description = "삭제된 라인 수", example = "20")
        public Long removeLines;

        @Schema(description = "첫 커밋 시간", example = "2025-01-01T09:00:00Z")
        public Instant firstCommit;

        @Schema(description = "마지막 커밋 시간", example = "2025-01-01T21:30:00Z")
        public Instant lastCommit;
    }

    public static class PullRequestDoc {
        @Schema(description = "PR 수", example = "1")
        public Integer count;

        @Schema(description = "대표 레포지토리", example = "clash-server")
        public String representationRepo;

        @Schema(description = "병합된 PR 수", example = "1")
        public Integer mergedCount;

        @Schema(description = "열린 PR 수", example = "0")
        public Integer openCount;

        @Schema(description = "닫힌 PR 수", example = "0")
        public Integer closedCount;
    }

    public static class IssueDoc {
        @Schema(description = "이슈 수", example = "0")
        public Integer count;
    }

    public static class ReviewDoc {
        @Schema(description = "리뷰 수", example = "1")
        public Integer count;
    }
}