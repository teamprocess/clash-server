package com.process.clash.adapter.web.user.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "GitHub OAuth 연동 응답",
        example = """
            {
              \"success\": true,
              \"message\": \"GitHub 계정 연동을 완료했습니다.\",
              \"data\": {
                \"gitHubId\": \"octocat\",
                \"linked\": true
              }
            }
            """
)
public class LinkGitHubOAuthResponseDoc extends SuccessResponseDoc {

    @Schema(description = "GitHub 연동 결과")
    public LinkGitHubOAuthDataDoc data;
}
