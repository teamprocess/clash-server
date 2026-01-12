package com.process.clash.adapter.persistence.roadmap;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserChapterProgressJpaRepository extends JpaRepository<UserChapterProgressJpaEntity, Long> {
	List<UserChapterProgressJpaEntity> findAllByUserId(Long userId);
}
