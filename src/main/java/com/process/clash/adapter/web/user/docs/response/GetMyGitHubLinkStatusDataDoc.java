package com.process.clash.adapter.web.user.docs.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "GitHub 연동 상태 데이터")
public class GetMyGitHubLinkStatusDataDoc {

    @Schema(description = "연동 여부", example = "true")
    public boolean linked;

    @Schema(description = "GitHub 사용자 ID", example = "octocat", nullable = true)
    public String gitHubId;
}
