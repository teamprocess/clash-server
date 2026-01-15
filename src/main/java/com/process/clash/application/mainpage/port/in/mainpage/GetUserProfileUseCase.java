package com.process.clash.application.mainpage.port.in.mainpage;

import com.process.clash.application.mainpage.data.GetUserProfileData;

public interface GetUserProfileUseCase {

    GetUserProfileData.Result execute(GetUserProfileData.Command command);
}
