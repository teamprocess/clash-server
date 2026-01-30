package com.process.clash.application.profile.port.in;

import com.process.clash.application.profile.data.GetMyActivityCalendarData;

public interface GetMyActivityCalendarUsecase {
    GetMyActivityCalendarData.Result execute(GetMyActivityCalendarData.Command command);
}
