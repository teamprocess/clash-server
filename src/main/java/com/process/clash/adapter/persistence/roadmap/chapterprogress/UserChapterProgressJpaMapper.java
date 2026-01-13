package com.process.clash.adapter.persistence.roadmap.chapterprogress;

import com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaRepository;
import com.process.clash.adapter.persistence.user.UserJpaRepository;
import com.process.clash.domain.roadmap.UserChapterProgress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserChapterProgressJpaMapper {

    private final ChapterJpaRepository chapterJpaRepository;
    private final UserJpaRepository userJpaRepository;

    public UserChapterProgressJpaEntity toJpaEntity(UserChapterProgress progress) {
        return new UserChapterProgressJpaEntity(
                progress.getId(),
                userJpaRepository.getReferenceById(progress.getUserId()),
                chapterJpaRepository.getReferenceById(progress.getChapterId()),
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
