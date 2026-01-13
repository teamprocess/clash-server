package com.process.clash.adapter.persistence.roadmap.chapterprogress;

import com.process.clash.application.roadmap.port.out.UserChapterProgressRepositoryPort;
import com.process.clash.domain.roadmap.UserChapterProgress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserChapterProgressPersistenceAdapter implements UserChapterProgressRepositoryPort {

    private final UserChapterProgressJpaRepository userChapterProgressJpaRepository;
    private final UserChapterProgressJpaMapper userChapterProgressJpaMapper;
    private final com.process.clash.adapter.persistence.user.UserJpaRepository userJpaRepository;
    private final com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaRepository chapterJpaRepository;

    @Override
    public void save(UserChapterProgress progress) {
        com.process.clash.adapter.persistence.user.UserJpaEntity userEntity = userJpaRepository.getReferenceById(progress.getUserId());
        com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaEntity chapterEntity = chapterJpaRepository.getReferenceById(progress.getChapterId());
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
