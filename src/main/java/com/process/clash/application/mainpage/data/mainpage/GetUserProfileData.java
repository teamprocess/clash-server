package com.process.clash.application.mainpage.data.mainpage;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.user.user.entity.User;

public class GetUserProfileData {
    public record Command(
            Actor actor
    ) {
        public static Command from(Actor actor) {
            return new Command(actor);
        }
    }

    public record Result(
            Long id,
            String name,
            String username,
            String profileImage
    ) {
        public static Result from(User user) {
            return new Result(
                    user.id(),
                    user.name(),
                    user.username(),
                    user.profileImage()
            );
        }
    }
}
