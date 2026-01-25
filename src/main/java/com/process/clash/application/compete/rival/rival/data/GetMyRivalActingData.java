package com.process.clash.application.compete.rival.rival.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.rival.rival.enums.RivalCurrentStatus;
import com.process.clash.domain.user.user.entity.User;

import java.util.List;

public class GetMyRivalActingData {

    public record Command(
            Actor actor
    ) {
        public static Command from(Actor actor) {
            return new Command(actor);
        }
    }

    public record Result(
            List<MyRival> myRivals
    ) {

        public static GetMyRivalActingData.Result from(List<MyRival> myRivals) {

            return new Result(
                    myRivals
            );
        }
    }

    public record MyRival(
            String name,
            String username,
            String profileImage,
            Long activeTime,
            String usingApp,
            RivalCurrentStatus status
    ) {

        public static MyRival of(User user, Long activeTime, String usingApp, RivalCurrentStatus status) {

            return new MyRival(
                    user.name(),
                    user.username(),
                    user.profileImage(),
                    activeTime,
                    usingApp,
                    status
            );
        }
    }
}
