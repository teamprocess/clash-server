package com.process.clash.application.profile.port.in;

import com.process.clash.application.profile.data.GetMyProfileData;

public interface GetMyProfileUsecase {
    GetMyProfileData.Result execute(GetMyProfileData.Command command);
}
