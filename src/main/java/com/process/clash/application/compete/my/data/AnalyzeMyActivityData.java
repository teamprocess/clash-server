package com.process.clash.application.compete.my.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.common.enums.TargetCategory;

import java.util.List;
import java.util.Map;

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

        public static Result of(TargetCategory category, Map<List<Streak>, List<Variation>> result) {

            Map.Entry<List<Streak>, List<Variation>> entry = result.entrySet().iterator().next();

            return new Result(
                    category.toString(),
                    entry.getKey(),
                    entry.getValue()
            );
        }
    }
}
