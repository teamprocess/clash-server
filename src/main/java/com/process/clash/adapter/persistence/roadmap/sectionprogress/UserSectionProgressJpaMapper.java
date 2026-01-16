package com.process.clash.adapter.persistence.roadmap.sectionprogress;

import com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaEntity;
import com.process.clash.adapter.persistence.roadmap.section.SectionJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.domain.roadmap.entity.UserSectionProgress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSectionProgressJpaMapper {

    public UserSectionProgressJpaEntity toJpaEntity(
            UserSectionProgress progress,
            UserJpaEntity userEntity,
            SectionJpaEntity sectionEntity,
            ChapterJpaEntity currentChapterEntity) {
        return new UserSectionProgressJpaEntity(
                progress.getId(),
                userEntity,
                sectionEntity,
                currentChapterEntity,
                progress.getCompletedChapters(),
                null, // createdAt은 @CreationTimestamp가 자동으로 설정
                null  // updatedAt은 @UpdateTimestamp가 자동으로 설정
        );
    }

    public UserSectionProgress toDomain(UserSectionProgressJpaEntity entity) {
        return new UserSectionProgress(
                entity.getId(),
                entity.getUser() != null ? entity.getUser().getId() : null,
                entity.getSection() != null ? entity.getSection().getId() : null,
                entity.getCurrentChapter() != null ? entity.getCurrentChapter().getId() : null,
                entity.getCompletedChapters()
        );
    }
}
