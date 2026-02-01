package com.process.clash.adapter.web.user.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "GitHub 연동 상태 조회 응답",
        example = """
            {
              \"success\": true,
              \"message\": \"GitHub 연동 상태를 성공적으로 조회했습니다.\",
              \"data\": {
                \"linked\": true,
                \"gitHubId\": \"octocat\"
              }
            }
            """
)
public class GetMyGitHubLinkStatusResponseDoc extends SuccessResponseDoc {

    @Schema(description = "GitHub 연동 상태")
    public GetMyGitHubLinkStatusDataDoc data;
}
