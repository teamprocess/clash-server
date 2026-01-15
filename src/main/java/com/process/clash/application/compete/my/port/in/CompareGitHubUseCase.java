package com.process.clash.application.compete.my.port.in;

import com.process.clash.application.compete.my.data.CompareGitHubData;

public interface CompareGitHubUseCase {

    CompareGitHubData.Result execute(CompareGitHubData.Command command);
}
