package com.process.clash.application.mainpage.port.in.compare;

import com.process.clash.application.compete.my.data.CompareGitHubData;

public interface CompareGitHubUseCase {

    CompareGitHubData.Result execute(CompareGitHubData.Command command);
}
