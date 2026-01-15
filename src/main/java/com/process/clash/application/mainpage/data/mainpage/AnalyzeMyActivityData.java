package com.process.clash.application.mainpage.data.mainpage;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.common.enums.TargetCategory;
import com.process.clash.domain.common.enums.WeekCategory;

import java.time.LocalDate;
import java.util.List;

public class AnalyzeMyActivityData {

    public record Command(
            Actor actor,
            TargetCategory category
    ) {

        public static Command from(Actor actor, TargetCategory category) {

            return new Command(
                    actor,
                    category
            );
        }
    }

    public record Result(
            TargetCategory category,
            List<Streak> streaks,
            List<Variation> variations
    ) {

//        public static Result from() {
//
//        }
    }

    public record Streak(
            LocalDate date,
            Integer detailedInfo,
            WeekCategory dayOfTheWeek
    ) {}

    public record Variation(
            Integer month,
            Double avgVariationPerMonth
    ) {}
}
