package com.process.clash.application.user.usergithub.data;

import com.process.clash.application.common.actor.Actor;

public class GetMyGitHubLinkStatusData {

    public record Command(
            Actor actor
    ) {
    }

    public record Result(
            boolean linked,
            String gitHubId
    ) {
    }
}
