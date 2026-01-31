package com.process.clash.application.user.usergithub.port.in;

import com.process.clash.application.user.usergithub.data.GetMyGitHubLinkStatusData;

public interface GetMyGitHubLinkStatusUsecase {

    GetMyGitHubLinkStatusData.Result execute(GetMyGitHubLinkStatusData.Command command);
}
