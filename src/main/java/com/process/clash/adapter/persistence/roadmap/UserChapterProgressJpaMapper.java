package com.process.clash.adapter.persistence.roadmap;

import com.process.clash.adapter.persistence.user.UserJpaEntity;
import com.process.clash.domain.roadmap.UserChapterProgress;
import org.springframework.stereotype.Component;

@Component
public class UserChapterProgressJpaMapper {

    public UserChapterProgressJpaEntity toJpaEntity(UserChapterProgress progress) {
        UserChapterProgressJpaEntity e = progress.getId() != null ? UserChapterProgressJpaEntity.ofId(progress.getId()) : new UserChapterProgressJpaEntity();
        if (progress.getUserId() != null) {
            e.setUser(UserJpaEntity.ofId(progress.getUserId()));
        }
        if (progress.getChapterId() != null) {
            e.setChapter(ChapterJpaEntity.ofId(progress.getChapterId()));
        }
        e.setStatus(progress.getStatus());
        return e;
    }
    public UserChapterProgress toDomain(UserChapterProgressJpaEntity entity) {
        return new UserChapterProgress(
                entity.getId(),
                entity.getUser() != null ? entity.getUser().getId() : null,
                entity.getChapter() != null ? entity.getChapter().getId() : null,
                entity.getStatus()
        );
    }
}
