package com.process.clash.application.profile.port.in;

import com.process.clash.application.profile.data.GetMyCalendarData;

public interface GetMyCalendarUsecase {
    GetMyCalendarData.Result execute(GetMyCalendarData.Command command);
}
