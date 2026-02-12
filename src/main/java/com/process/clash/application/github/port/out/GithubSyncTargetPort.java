package com.process.clash.application.github.port.out;

import com.process.clash.application.github.model.GithubSyncTarget;

import java.util.List;
import java.util.Optional;

public interface GithubSyncTargetPort {
    List<GithubSyncTarget> findSyncTargets();

    Optional<GithubSyncTarget> findSyncTargetByUserId(Long userId);
}
