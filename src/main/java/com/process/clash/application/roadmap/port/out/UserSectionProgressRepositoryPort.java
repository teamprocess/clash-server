package com.process.clash.application.roadmap.port.out;

import com.process.clash.domain.roadmap.entity.UserSectionProgress;

import java.util.List;
import java.util.Optional;

public interface UserSectionProgressRepositoryPort {
    void save(UserSectionProgress progress);
    Optional<UserSectionProgress> findById(Long id);
    Optional<UserSectionProgress> findByUserIdAndSectionId(Long userId, Long sectionId);
    List<UserSectionProgress> findAllByUserId(Long userId);
    List<UserSectionProgress> findAllByUserIdAndSectionIdIn(Long userId, List<Long> sectionIds);
}
