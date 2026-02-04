package com.process.clash.adapter.persistence.roadmap.v2.questionhistory;

import com.process.clash.adapter.persistence.roadmap.v2.chapter.ChapterV2JpaEntity;
import com.process.clash.adapter.persistence.roadmap.v2.chapter.ChapterV2JpaRepository;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.roadmap.v2.port.out.UserQuestionHistoryV2RepositoryPort;
import com.process.clash.domain.roadmap.v2.entity.UserQuestionHistoryV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserQuestionHistoryV2PersistenceAdapter implements UserQuestionHistoryV2RepositoryPort {

    private final UserQuestionHistoryV2JpaRepository userQuestionHistoryV2JpaRepository;
    private final UserQuestionHistoryV2JpaMapper userQuestionHistoryV2JpaMapper;
    private final UserJpaRepository userJpaRepository;
    private final ChapterV2JpaRepository chapterV2JpaRepository;

    @Override
    public UserQuestionHistoryV2 save(UserQuestionHistoryV2 history) {
        UserJpaEntity userEntity = userJpaRepository.getReferenceById(history.getUserId());
        ChapterV2JpaEntity chapterEntity = chapterV2JpaRepository.getReferenceById(history.getChapterId());
        UserQuestionHistoryV2JpaEntity savedEntity = userQuestionHistoryV2JpaRepository.save(
                userQuestionHistoryV2JpaMapper.toJpaEntity(history, userEntity, chapterEntity)
        );
        return userQuestionHistoryV2JpaMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<UserQuestionHistoryV2> findById(Long id) {
        return userQuestionHistoryV2JpaRepository.findById(id).map(userQuestionHistoryV2JpaMapper::toDomain);
    }

    @Override
    public List<UserQuestionHistoryV2> findAllByUserId(Long userId) {
        return userQuestionHistoryV2JpaRepository.findAllByUserId(userId).stream()
                .map(userQuestionHistoryV2JpaMapper::toDomain).toList();
    }

    @Override
    public Optional<UserQuestionHistoryV2> findByUserIdAndChapterId(Long userId, Long chapterId) {
        return userQuestionHistoryV2JpaRepository.findByUserIdAndChapterId(userId, chapterId)
                .map(userQuestionHistoryV2JpaMapper::toDomain);
    }

    @Override
    public List<UserQuestionHistoryV2> findAllByUserIdAndChapterIdIn(Long userId, List<Long> chapterIds) {
        return userQuestionHistoryV2JpaRepository.findAllByUserIdAndChapterIdIn(userId, chapterIds).stream()
                .map(userQuestionHistoryV2JpaMapper::toDomain).toList();
    }
}
