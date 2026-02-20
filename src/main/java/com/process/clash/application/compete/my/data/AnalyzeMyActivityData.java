package com.process.clash.application.compete.my.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.common.enums.TargetCategory;

import java.util.List;

public class AnalyzeMyActivityData {

    public record Command(
            Actor actor,
            TargetCategory category
    ) {

        public static Command of(Actor actor, TargetCategory category) {

            return new Command(
                    actor,
                    category
            );
        }
    }

    public record Result(
            String category,
            List<Streak> streaks,
            List<Variation> variations
    ) {

        public static Result of(TargetCategory category, List<Streak> streaks, List<Variation> variations) {

            return new Result(
                    category.toString(),
                    streaks,
                    variations
            );
        }
    }
}
