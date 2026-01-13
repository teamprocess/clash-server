package com.process.clash.adapter.persistence.roadmap.chapterprogress;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaEntity;
import com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaRepository;
import com.process.clash.adapter.persistence.user.UserJpaEntity;
import com.process.clash.adapter.persistence.user.UserJpaRepository;
import com.process.clash.application.roadmap.port.out.UserChapterProgressRepositoryPort;
import com.process.clash.domain.roadmap.model.entity.UserChapterProgress;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserChapterProgressPersistenceAdapter implements UserChapterProgressRepositoryPort {

    private final UserChapterProgressJpaRepository userChapterProgressJpaRepository;
    private final UserChapterProgressJpaMapper userChapterProgressJpaMapper;
    private final UserJpaRepository userJpaRepository;
    private final ChapterJpaRepository chapterJpaRepository;

    @Override
    public void save(UserChapterProgress progress) {
        UserJpaEntity userEntity = userJpaRepository.getReferenceById(progress.getUserId());
        ChapterJpaEntity chapterEntity = chapterJpaRepository.getReferenceById(progress.getChapterId());
        userChapterProgressJpaRepository.save(userChapterProgressJpaMapper.toJpaEntity(progress, userEntity, chapterEntity));
    }

    @Override
    public Optional<UserChapterProgress> findById(Long id) {
        return userChapterProgressJpaRepository.findById(id).map(userChapterProgressJpaMapper::toDomain);
    }

    @Override
    public List<UserChapterProgress> findAllByUserId(Long userId) {
        return userChapterProgressJpaRepository.findAllByUserId(userId).stream().map(userChapterProgressJpaMapper::toDomain).toList();
    }
}
