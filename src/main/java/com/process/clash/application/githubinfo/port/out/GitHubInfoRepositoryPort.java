package com.process.clash.application.githubinfo.port.out;

import com.process.clash.domain.githubinfo.entity.GitHubInfo;

import java.time.LocalDate;
import java.util.Optional;

public interface GitHubInfoRepositoryPort {

    GitHubInfo save(GitHubInfo gitHubInfo);
    Optional<GitHubInfo> findByUserIdAndDate(Long userId, LocalDate date);
}
