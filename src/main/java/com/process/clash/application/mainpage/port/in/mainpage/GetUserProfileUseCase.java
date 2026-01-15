package com.process.clash.application.mainpage.port.in.mainpage;

import com.process.clash.application.mainpage.data.mainpage.GetUserProfileData;

public interface GetUserProfileUseCase {

    GetUserProfileData.Result execute(GetUserProfileData.Command command);
}
