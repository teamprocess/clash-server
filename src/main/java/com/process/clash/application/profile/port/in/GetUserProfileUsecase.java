package com.process.clash.application.profile.port.in;

import com.process.clash.application.profile.data.GetUserProfileData;

public interface GetUserProfileUsecase {
    GetUserProfileData.Result execute(GetUserProfileData.Command command);
}
