package com.process.clash.application.roadmap.port.out;

import com.process.clash.domain.roadmap.UserChapterProgress;

import java.util.List;
import java.util.Optional;

public interface UserChapterProgressRepositoryPort {
    void save(UserChapterProgress progress);
    Optional<UserChapterProgress> findById(Long id);
    List<UserChapterProgress> findAllByUserId(Long userId);
}
