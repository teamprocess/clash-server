package com.process.clash.application.compete.rival.rival.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.rival.rival.enums.RivalLinkingStatus;
import com.process.clash.domain.user.user.entity.User;

import java.util.List;

public class FindAllRivalsData {

    public record Command(
            Actor actor
    ) {
        public static Command from(Actor actor) {
            return new Command(actor);
        }
    }

    public record Result(
            List<RivalInfo> rivals
    ) {
        public static Result from(List<RivalInfo> rivals) {
            return new Result(rivals);
        }
    }

    public record RivalInfo(
            Long rivalId,
            String githubId,
            String name,
            String profileImage,
            RivalLinkingStatus rivalLinkingStatus
    ) {
        public static RivalInfo of(Long rivalId, User user, RivalLinkingStatus rivalLinkingStatus) {
            return new RivalInfo(
                    rivalId,
                    user.username(),
                    user.name(),
                    user.profileImage(),
                    rivalLinkingStatus
            );
        }
    }
}
