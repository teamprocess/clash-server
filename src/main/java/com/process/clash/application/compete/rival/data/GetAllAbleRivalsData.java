package com.process.clash.application.compete.rival.data;

import com.process.clash.application.common.actor.Actor;

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
            List<AbleRivalInfo> users
    ) {

        public static Result from(List<AbleRivalInfo> ableRivalInfos) {
            return new Result(
                    ableRivalInfos
            );
        }
    }
}