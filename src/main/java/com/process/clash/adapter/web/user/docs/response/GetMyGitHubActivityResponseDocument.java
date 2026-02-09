package com.process.clash.adapter.web.user.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "특정 기간 GitHub 활동 조회 응답",
        example = """
            {
              "success": true,
              "message": "특정 기간 동안의 깃허브 활동을 성공적으로 조회했습니다.",
              "data": {
                "totalContributions": 42,
                "contributions": [
                  {
                    "date": "2025-01-01",
                    "contributionCount": 3,
                    "contributionLevel": 1
                  }
                ]
              }
            }
            """
)
public class GetMyGitHubActivityResponseDocument extends SuccessResponseDocument {

    @Schema(description = "GitHub 활동")
    public GetMyGitHubActivityDataDocument data;
}
