package com.process.clash.application.ranking.data;

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
            String category,
            String period,
            List<UserRanking> rankings
    ) {

        public static Result of(TargetCategory category, PeriodCategory period, List<UserRanking> rankings) {

            return new Result(
                    category.toString(),
                    period.toString(),
                    rankings
            );
        }
    }
}
