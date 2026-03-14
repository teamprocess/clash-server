package com.process.clash.application.compete.rival.rival.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.realtime.data.UserActivityStatus;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.userrankhistory.enums.ExpTier;
import com.process.clash.domain.user.userrankhistory.enums.RankTier;

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
            Long rivalId,
            Long id,
            String name,
            String username,
            String profileImage,
            Long activeTime,
            String usingApp,
            Boolean isStudying,
            UserActivityStatus status,
            RankTier rankTier,
            ExpTier expTier
    ) {

        public static MyRival of(
            Long rivalId,
            User user,
            Long activeTime,
            String usingApp,
            Boolean isStudying,
            UserActivityStatus status
        ) {

            return new MyRival(
                    rivalId,
                    user.id(),
                    user.name(),
                    user.username(),
                    user.profileImage(),
                    activeTime,
                    usingApp,
                    isStudying,
                    status,
                    user.currentRankTier(),
                    user.currentExpTier()
            );
        }
    }
}
