package com.process.clash.adapter.persistence.roadmap.v2.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionV2JpaRepository extends JpaRepository<QuestionV2JpaEntity, Long> {

    List<QuestionV2JpaEntity> findAllByChapterIdOrderByOrderIndexAsc(Long chapterId);
}
