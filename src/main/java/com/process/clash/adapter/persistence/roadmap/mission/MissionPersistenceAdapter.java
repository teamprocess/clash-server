package com.process.clash.adapter.persistence.roadmap.mission;

import com.process.clash.application.roadmap.port.out.MissionRepositoryPort;
import com.process.clash.domain.roadmap.Mission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MissionPersistenceAdapter implements MissionRepositoryPort {

    private final MissionJpaRepository missionJpaRepository;
    private final MissionJpaMapper missionJpaMapper;

    @Override
    public void save(Mission mission) {
        missionJpaRepository.save(missionJpaMapper.toJpaEntity(mission));
    }

    @Override
    public Optional<Mission> findById(Long id) {
        return missionJpaRepository.findById(id).map(missionJpaMapper::toDomain);
    }

    @Override
    public List<Mission> findAll() {
        return missionJpaRepository.findAll().stream().map(missionJpaMapper::toDomain).toList();
    }

    @Override
    public List<Mission> findAllByChapterId(Long chapterId) {
        return missionJpaRepository.findAllByChapterId(chapterId).stream().map(missionJpaMapper::toDomain).toList();
    }
}
