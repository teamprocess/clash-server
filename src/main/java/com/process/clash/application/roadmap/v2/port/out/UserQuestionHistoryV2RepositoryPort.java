package com.process.clash.application.roadmap.v2.port.out;

import com.process.clash.domain.roadmap.v2.entity.UserQuestionHistoryV2;

import java.util.List;
import java.util.Optional;

public interface UserQuestionHistoryV2RepositoryPort {
    UserQuestionHistoryV2 save(UserQuestionHistoryV2 history);
    Optional<UserQuestionHistoryV2> findById(Long id);
    List<UserQuestionHistoryV2> findAllByUserId(Long userId);
    Optional<UserQuestionHistoryV2> findByUserIdAndChapterId(Long userId, Long chapterId);
    List<UserQuestionHistoryV2> findAllByUserIdAndChapterIdIn(Long userId, List<Long> chapterIds);
}
