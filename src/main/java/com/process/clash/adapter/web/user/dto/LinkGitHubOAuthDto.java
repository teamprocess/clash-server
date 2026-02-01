package com.process.clash.adapter.web.user.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.user.usergithub.data.LinkGitHubOAuthData;

public class LinkGitHubOAuthDto {

    public record Request(
            String code
    ) {
        public LinkGitHubOAuthData.Command toCommand(Actor actor) {
            return new LinkGitHubOAuthData.Command(actor, code);
        }
    }

    public record Response(
            String gitHubId,
            boolean linked
    ) {
        public static Response from(LinkGitHubOAuthData.Result result) {
            return new Response(result.gitHubId(), result.linked());
        }
    }
}
