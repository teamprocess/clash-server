package com.process.clash.adapter.persistence.roadmap.sectionprogress;

import com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaEntity;
import com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaRepository;
import com.process.clash.adapter.persistence.roadmap.section.SectionJpaEntity;
import com.process.clash.adapter.persistence.roadmap.section.SectionJpaRepository;
import com.process.clash.adapter.persistence.user.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.user.UserJpaRepository;
import com.process.clash.application.roadmap.port.out.UserSectionProgressRepositoryPort;
import com.process.clash.domain.roadmap.entity.UserSectionProgress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserSectionProgressPersistenceAdapter implements UserSectionProgressRepositoryPort {

    private final UserSectionProgressJpaRepository userSectionProgressJpaRepository;
    private final UserSectionProgressJpaMapper userSectionProgressJpaMapper;
    private final UserJpaRepository userJpaRepository;
    private final SectionJpaRepository sectionJpaRepository;
    private final ChapterJpaRepository chapterJpaRepository;

    @Override
    public void save(UserSectionProgress progress) {
        UserJpaEntity userEntity = userJpaRepository.getReferenceById(progress.getUserId());
        SectionJpaEntity sectionEntity = sectionJpaRepository.getReferenceById(progress.getSectionId());
        ChapterJpaEntity currentChapterEntity = progress.getCurrentChapterId() != null
                ? chapterJpaRepository.getReferenceById(progress.getCurrentChapterId())
                : null;
        userSectionProgressJpaRepository.save(
                userSectionProgressJpaMapper.toJpaEntity(progress, userEntity, sectionEntity, currentChapterEntity)
        );
    }

    @Override
    public Optional<UserSectionProgress> findById(Long id) {
        return userSectionProgressJpaRepository.findById(id)
                .map(userSectionProgressJpaMapper::toDomain);
    }

    @Override
    public Optional<UserSectionProgress> findByUserIdAndSectionId(Long userId, Long sectionId) {
        return userSectionProgressJpaRepository.findByUserIdAndSectionId(userId, sectionId)
                .map(userSectionProgressJpaMapper::toDomain);
    }

    @Override
    public List<UserSectionProgress> findAllByUserId(Long userId) {
        return userSectionProgressJpaRepository.findAllByUserId(userId).stream()
                .map(userSectionProgressJpaMapper::toDomain)
                .toList();
    }

    @Override
    public List<UserSectionProgress> findAllByUserIdAndSectionIdIn(Long userId, List<Long> sectionIds) {
        return userSectionProgressJpaRepository.findAllByUserIdAndSectionIdIn(userId, sectionIds).stream()
                .map(userSectionProgressJpaMapper::toDomain)
                .toList();
    }
}
