package com.process.clash.application.user.user.port.in;

import com.process.clash.application.user.user.data.UpdateMyPrivacyData;

public interface UpdateMyPrivacyUseCase {
    UpdateMyPrivacyData.Result execute(UpdateMyPrivacyData.Command command);
}
