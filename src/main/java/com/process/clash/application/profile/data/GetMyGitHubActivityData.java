package com.process.clash.application.profile.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.common.enums.PeriodCategory;
import java.util.List;

public class GetMyGitHubActivityData {

    public record Command(
        Actor actor,
        PeriodCategory period
    ) {}

    public record Result(
        int totalContributions,
        List<Contribution> contributions
    ) {
        public static Result from(int totalContributions, List<Contribution> contributions) {
            return new Result(totalContributions, contributions);
        }
    }

    public record Contribution(
        String date,
        int contributionCount,
        int contributionLevel
    ) {
        public static Contribution of(String date, int contributionCount, int contributionLevel) {
            return new Contribution(date, contributionCount, contributionLevel);
        }
    }
}
