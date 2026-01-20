package com.process.clash.application.compete.rival.battle.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.rival.data.AbleRivalInfoForBattle;

import java.util.List;

public class FindAbleRivalsData {

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
            List<AbleRivalInfoForBattle> rivals
    ) {

        public static Result from(List<AbleRivalInfoForBattle> rivals) {

            return new Result(
                    rivals
            );
        }
    }
}
