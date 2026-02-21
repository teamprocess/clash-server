package com.process.clash.application.user.usernotice.port.in;

import com.process.clash.application.user.usernotice.data.GetMyUserNoticesData;

public interface GetMyUserNoticesUseCase {

    GetMyUserNoticesData.Result execute(GetMyUserNoticesData.Command command);
}
