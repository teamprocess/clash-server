package com.process.clash.adapter.web.user.docs.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "GitHub OAuth 연동 데이터")
public class LinkGitHubOAuthDataDoc {

    @Schema(description = "GitHub 사용자 ID", example = "octocat")
    public String gitHubId;

    @Schema(description = "연동 여부", example = "true")
    public boolean linked;
}
