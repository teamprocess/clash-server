package com.process.clash.application.compete.rival.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.usergithub.entity.UserGitHub;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        public static Result from(List<User> users, List<UserGitHub> userGitHubs) {
            Map<Long, User> userMap = users.stream()
                    .collect(Collectors.toMap(User::id, user -> user));

            List<UserInfo> userInfos = userGitHubs.stream()
                    .map(userGitHub -> {
                        User user = userMap.get(userGitHub.userId());
                        return new UserInfo(
                                user.id(),
                                user.name(),
                                userGitHub.gitHubId(),
                                user.profileImage()
                        );
                    })
                    .toList();

            return new Result(userInfos);
        }
    }

    public record UserInfo(
            Long id,
            String name,
            String githubId,
            String profileImage
    ) {}
}