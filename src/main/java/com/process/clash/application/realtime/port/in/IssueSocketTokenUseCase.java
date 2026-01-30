package com.process.clash.application.realtime.port.in;

import com.process.clash.application.realtime.data.IssueSocketTokenData;

public interface IssueSocketTokenUseCase {
    IssueSocketTokenData.Result issueToken(Long userId);
}
