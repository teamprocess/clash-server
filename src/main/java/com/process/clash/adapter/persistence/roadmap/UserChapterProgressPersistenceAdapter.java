package com.process.clash.adapter.persistence.roadmap;

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

    @Override
    public void save(UserChapterProgress progress) {
        userChapterProgressJpaRepository.save(userChapterProgressJpaMapper.toJpaEntity(progress));
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
