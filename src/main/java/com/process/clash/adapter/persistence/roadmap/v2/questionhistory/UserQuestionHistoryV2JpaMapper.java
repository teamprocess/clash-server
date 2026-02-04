package com.process.clash.adapter.persistence.roadmap.v2.questionhistory;

import com.process.clash.adapter.persistence.roadmap.v2.chapter.ChapterV2JpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.roadmap.v2.entity.UserQuestionHistoryV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQuestionHistoryV2JpaMapper {

    public UserQuestionHistoryV2JpaEntity toJpaEntity(UserQuestionHistoryV2 history, UserJpaEntity userEntity, ChapterV2JpaEntity chapterEntity) {
        return new UserQuestionHistoryV2JpaEntity(
                history.getId(),
                userEntity,
                chapterEntity,
                history.isCleared(),
                history.getCorrectCount(),
                history.getTotalCount(),
                history.getCurrentQuestionIndex(),
                null, // createdAt은 @CreationTimestamp가 자동으로 설정
                null  // updatedAt은 @UpdateTimestamp가 자동으로 설정
        );
    }

    public UserQuestionHistoryV2 toDomain(UserQuestionHistoryV2JpaEntity entity) {
        return new UserQuestionHistoryV2(
                entity.getId(),
                entity.getUser() != null ? entity.getUser().getId() : null,
                entity.getChapter() != null ? entity.getChapter().getId() : null,
                entity.isCleared(),
                entity.getCorrectCount(),
                entity.getTotalCount(),
                entity.getCurrentQuestionIndex()
        );
    }
}
