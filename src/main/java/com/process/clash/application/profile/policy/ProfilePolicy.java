package com.process.clash.application.profile.policy;

import com.process.clash.application.user.user.exception.exception.badrequest.InvalidPeriodCategoryException;
import com.process.clash.domain.common.enums.PeriodCategory;
import org.springframework.stereotype.Component;

@Component
public class ProfilePolicy {

    public void validateGithubPeriod(PeriodCategory period) {
        if (period == null || period == PeriodCategory.DAY || period == PeriodCategory.SEASON) {
            throw new InvalidPeriodCategoryException();
        }
    }
}
