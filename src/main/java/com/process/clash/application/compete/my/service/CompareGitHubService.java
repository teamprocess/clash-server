package com.process.clash.application.compete.my.service;

import com.process.clash.application.compete.my.data.CompareGitHubData;
import com.process.clash.application.compete.my.port.in.CompareGitHubUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompareGitHubService implements CompareGitHubUseCase {

    @Override
    public CompareGitHubData.Result execute(CompareGitHubData.Command command) {

        return null;
    }
}
