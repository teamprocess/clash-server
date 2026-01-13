package com.process.clash.adapter.persistence.roadmap.chapterprogress;

import com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaEntity;
import com.process.clash.adapter.persistence.user.UserJpaEntity;
import com.process.clash.domain.roadmap.UserChapterProgress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserChapterProgressJpaMapper {

    public UserChapterProgressJpaEntity toJpaEntity(UserChapterProgress progress, UserJpaEntity userEntity, ChapterJpaEntity chapterEntity) {
        return new UserChapterProgressJpaEntity(
                progress.getId(),
                userEntity,
                chapterEntity,
                progress.getStatus()
        );
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
