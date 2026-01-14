package com.process.clash.application.mainpage.data;

import com.process.clash.application.common.actor.Actor;

public class GetCompareWithYesterdayData {

    public record Command(
            Actor actor
    ) {
        public static GetCompareWithYesterdayData.Command from(Actor actor) {
            return new GetCompareWithYesterdayData.Command(
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
