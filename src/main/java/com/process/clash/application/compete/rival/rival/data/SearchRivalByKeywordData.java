package com.process.clash.application.compete.rival.rival.data;

import com.process.clash.application.common.actor.Actor;

import java.util.List;

public class SearchRivalByKeywordData {

    public record Command(
            Actor actor,
            String keyword
    ) {

        public static Command from(Actor actor, String keyword) {

            return new Command(
                    actor,
                    keyword
            );
        }
    }

    public record Result(
            List<AbleRivalInfoForRival> users
    ) {

        public static Result from(List<AbleRivalInfoForRival> ableRivalInfoForRivals) {
            return new Result(
                    ableRivalInfoForRivals
            );
        }
    }
}
