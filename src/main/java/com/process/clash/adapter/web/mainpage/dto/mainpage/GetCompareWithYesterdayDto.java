package com.process.clash.adapter.web.mainpage.dto.mainpage;

import com.process.clash.application.mainpage.data.mainpage.GetCompareWithYesterdayData;

public class GetCompareWithYesterdayDto {

    public record Response(
            ActiveTime activeTime,
            Contributors contributors
    ) {
        public static Response from(GetCompareWithYesterdayData.Result result) {
            return new Response(
                    ActiveTime.from(result.activeTime()),
                    Contributors.from(result.contributors())
            );
        }
    }

    public record ActiveTime(
            Integer yesterdayActiveTime,
            Integer todayActiveTime
    ) {
        public static ActiveTime from(GetCompareWithYesterdayData.ActiveTime data) {
            return new ActiveTime(
                    data.yesterdayActiveTime(),
                    data.todayActiveTime()
            );
        }
    }

    public record Contributors(
            Integer yesterdayContributors,
            Integer todayContributors
    ) {
        public static Contributors from(GetCompareWithYesterdayData.Contributors data) {
            return new Contributors(
                    data.yesterdayContributors(),
                    data.todayContributors()
            );
        }
    }
}