package com.process.clash.application.user.user.port.in;

import com.process.clash.application.user.user.data.UpdateMyProfileImageData;

public interface UpdateMyProfileImageUseCase {
    UpdateMyProfileImageData.Result execute(UpdateMyProfileImageData.Command command);
}
