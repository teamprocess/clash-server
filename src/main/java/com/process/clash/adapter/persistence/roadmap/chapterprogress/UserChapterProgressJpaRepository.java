package com.process.clash.adapter.persistence.roadmap.chapterprogress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserChapterProgressJpaRepository extends JpaRepository<UserChapterProgressJpaEntity, Long> {
	List<UserChapterProgressJpaEntity> findAllByUserId(Long userId);
}
