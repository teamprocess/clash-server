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

//        public static GetCompareWithYesterdayData.Result from() {
//
//        }
    }

    public record ActiveTime(
            Integer yesterdayActiveTime,
            Integer todayActiveTime
    ) {}

    public record Contributors(
            Integer yesterdayContributors,
            Integer todayContributors
    ) {}
}
