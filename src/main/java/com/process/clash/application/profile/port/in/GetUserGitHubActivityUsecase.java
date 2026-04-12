package com.process.clash.application.profile.port.in;

import com.process.clash.application.profile.data.GetMyGitHubActivityData;
import com.process.clash.application.profile.data.GetUserGitHubActivityData;

public interface GetUserGitHubActivityUsecase {
    GetMyGitHubActivityData.Result execute(GetUserGitHubActivityData.Command command);
}
