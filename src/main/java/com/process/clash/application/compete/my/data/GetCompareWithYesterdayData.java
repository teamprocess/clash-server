package com.process.clash.application.compete.my.data;

import com.process.clash.application.common.actor.Actor;

public class GetCompareWithYesterdayData {

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
            ActiveTime activeTime,
            Contributors contributors
    ) {

        public static Result of(Long yesterdayActiveTime, Long todayActiveTime, Integer yesterdayContributions, Integer todayContributions) {

            return new Result(
                    ActiveTime.of(
                            yesterdayActiveTime,
                            todayActiveTime
                    ),
                    Contributors.of(
                            yesterdayContributions,
                            todayContributions
                    )
            );
        }
    }

    public record ActiveTime(
            Long yesterdayActiveTime,
            Long todayActiveTime
    ) {

        public static ActiveTime of(Long yesterdayActiveTime, Long todayActiveTime) {

            return new ActiveTime(
                    yesterdayActiveTime,
                    todayActiveTime
            );
        }
    }

    public record Contributors(
            Integer yesterdayContributors,
            Integer todayContributors
    ) {

        public static Contributors of(Integer yesterdayContributors, Integer todayContributors) {

            return new Contributors(
                    yesterdayContributors,
                    todayContributors
            );
        }
    }
}
