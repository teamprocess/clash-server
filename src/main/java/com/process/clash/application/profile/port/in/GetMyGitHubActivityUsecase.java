package com.process.clash.application.profile.port.in;

import com.process.clash.application.profile.data.GetMyGitHubActivityData;

public interface GetMyGitHubActivityUsecase {
    GetMyGitHubActivityData.Result execute(GetMyGitHubActivityData.Command command);
}
