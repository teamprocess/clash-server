package com.process.clash.application.profile.data;

import com.process.clash.domain.common.enums.PeriodCategory;

public class GetUserGitHubActivityData {

    public record Command(
        Long targetUserId,
        PeriodCategory period
    ) {}
}
