package com.process.clash.adapter.web.mainpage.dto.mainpage;

import com.process.clash.application.mainpage.data.mainpage.GetCompareWithYesterdayData;

public class GetCompareWithYesterdayDto {

    public record Response(
            ActiveTime activeTime,
            Contributors contributors
    ) {

        public static Response from(GetCompareWithYesterdayData.Result result) {

            return new Response(
                    new ActiveTime(
                            result.activeTime().yesterdayActiveTime(),
                            result.activeTime().todayActiveTime()
                    ),
                    new Contributors(
                            result.contributors().yesterdayContributors(),
                            result.contributors().todayContributors()
                    )
            );
        }
    }

    private record ActiveTime(
            Integer yesterdayActiveTime,
            Integer todayActiveTime
    ) {}

    private record Contributors(
            Integer yesterdayContributors,
            Integer todayContributors
    ) {}
}