package com.process.clash.adapter.web.user.dto;

import com.process.clash.application.user.usergithub.data.GetMyGitHubLinkStatusData;

public class GetMyGitHubLinkStatusDto {

    public record Response(
            boolean linked,
            String gitHubId
    ) {
        public static Response from(GetMyGitHubLinkStatusData.Result result) {
            return new Response(result.linked(), result.gitHubId());
        }
    }
}
