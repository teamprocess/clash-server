package com.process.clash.application.profile.port.in;

import com.process.clash.application.profile.data.GetMyGitHubActivityDetailData;

public interface GetMyGitHubActivityDetailUsecase {
    GetMyGitHubActivityDetailData.Result execute(GetMyGitHubActivityDetailData.Command command);
}
