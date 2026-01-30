package com.process.clash.application.user.usergithub.data;

import com.process.clash.application.common.actor.Actor;

public class LinkGitHubOAuthData {

    public record Command(
            Actor actor,
            String code
    ) {
    }

    public record Result(
            String gitHubId,
            boolean linked
    ) {
    }
}
