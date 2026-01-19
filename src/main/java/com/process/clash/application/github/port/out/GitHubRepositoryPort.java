package com.process.clash.application.github.port.out;

import com.process.clash.domain.github.entity.GitHub;

public interface GitHubRepositoryPort {

    GitHub save(GitHub gitHub);
}
