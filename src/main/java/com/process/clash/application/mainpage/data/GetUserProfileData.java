package com.process.clash.application.mainpage.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.user.model.entity.User;

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
    ) {
        public static GetUserProfileData.Result from(User user) {
            return new GetUserProfileData.Result(
                    user.id(),
                    user.name(),
                    user.username(),
                    user.profileImage()
            );
        }
    }
}
