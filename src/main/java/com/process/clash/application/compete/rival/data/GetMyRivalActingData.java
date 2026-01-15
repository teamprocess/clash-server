package com.process.clash.application.compete.rival.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.rival.enums.RivalCurrentStatus;

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
//        public static GetMyRivalActingData.Result from() {
//
//        }
    }

    public record MyRival(
            String name,
            String username,
            String profileImage,
            String activeTime,
            String usingApp,
            RivalCurrentStatus status
    ) {}
}
