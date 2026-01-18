package com.process.clash.application.compete.rival.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.user.user.entity.User;

import java.util.List;

public class GetAllAbleRivalsData {

    public record Command(
            Actor actor
    ) {

        public static Command from(Actor actor) {

            return new Command(
                    actor
            );
        }
    }

    public record Result(
            List<UserInfo> users
    ) {

        public static Result from(List<User> users) {
            List<UserInfo> userInfos = users.stream()
                    .map(user -> new UserInfo(
                            user.id(),
                            user.name(),
                            user.githubId()
                    ))
                    .toList();

            return new Result(userInfos);
        }
    }

    public record UserInfo(
            Long id,
            String name,
            String githubId
    ) {}
}
