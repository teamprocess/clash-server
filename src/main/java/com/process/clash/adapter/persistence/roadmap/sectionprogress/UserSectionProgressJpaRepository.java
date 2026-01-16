package com.process.clash.adapter.persistence.roadmap.sectionprogress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSectionProgressJpaRepository extends JpaRepository<UserSectionProgressJpaEntity, Long> {
    Optional<UserSectionProgressJpaEntity> findByUserIdAndSectionId(Long userId, Long sectionId);
    List<UserSectionProgressJpaEntity> findAllByUserId(Long userId);
}
