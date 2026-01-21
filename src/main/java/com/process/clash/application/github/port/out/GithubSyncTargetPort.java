package com.process.clash.application.github.port.out;

import com.process.clash.application.github.model.GithubSyncTarget;

import java.util.List;

public interface GithubSyncTargetPort {
    List<GithubSyncTarget> findSyncTargets();
}
