package com.process.clash.adapter.persistence.roadmap.v2.questionhistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserQuestionHistoryV2JpaRepository extends JpaRepository<UserQuestionHistoryV2JpaEntity, Long> {
    List<UserQuestionHistoryV2JpaEntity> findAllByUserId(Long userId);
    Optional<UserQuestionHistoryV2JpaEntity> findByUserIdAndChapterId(Long userId, Long chapterId);
    List<UserQuestionHistoryV2JpaEntity> findAllByUserIdAndChapterIdIn(Long userId, List<Long> chapterIds);
}
