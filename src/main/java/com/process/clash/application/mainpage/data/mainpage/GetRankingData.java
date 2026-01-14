package com.process.clash.application.mainpage.data.mainpage;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.common.enums.PeriodCategory;
import com.process.clash.domain.common.enums.TargetCategory;

import java.util.List;

public class GetRankingData {

    public record Command(
            Actor actor,
            TargetCategory category,
            PeriodCategory period
    ) {

        public static Command from(Actor actor, TargetCategory category, PeriodCategory period) {

            return new Command(
                    actor,
                    category,
                    period
            );
        }
    }

    public record Result(
            TargetCategory category,
            PeriodCategory period,
            List<Ranking> rankings
    ) {

//        public static Result from() {
//
//        }
    }

    public record Ranking(
            String name,
            String username,
            String profileImage,
            Boolean isRival,
            String linkedId,
            Integer point
    ) {}
}
