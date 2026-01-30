package com.process.clash.adapter.web.user.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "특정 날짜 GitHub 활동 조회 응답",
        example = """
            {
              "success": true,
              "message": "특정 날짜의 깃허브 활동을 성공적으로 조회했습니다.",
              "data": {
                "date": "2025-01-01",
                "contributionCount": 4,
                "contributionLevel": 1,
                "commitsCount": 2,
                "issuesCount": 1,
                "prCount": 1,
                "reviewsCount": 0,
                "additionLines": 120,
                "deletionLines": 45
              }
            }
            """
)
public class GetMyGitHubActivityDetailResponseDoc extends SuccessResponseDoc {

    @Schema(description = "GitHub 활동 상세")
    public GetMyGitHubActivityDetailDataDoc data;
}
