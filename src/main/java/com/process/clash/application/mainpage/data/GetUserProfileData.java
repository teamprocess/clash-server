package com.process.clash.application.mainpage.data;

import com.process.clash.application.common.actor.Actor;

public class GetUserProfileData {
    public record Command(
            Actor actor
    ) {
        public static GetUserProfileData.Command from(Actor actor) {
            return new GetUserProfileData.Command(actor);
        }
    }

    public record Result(
            Long id,
            String name,
            String username,
            String profileImage
    ) {}
}
