package com.process.clash.adapter.persistence.roadmap.mission;

import com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaEntity;
import com.process.clash.application.roadmap.port.out.MissionRepositoryPort;
import com.process.clash.domain.roadmap.entity.Mission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MissionPersistenceAdapter implements MissionRepositoryPort {

    private final MissionJpaRepository missionJpaRepository;
    private final MissionJpaMapper missionJpaMapper;
    private final com.process.clash.adapter.persistence.roadmap.chapter.ChapterJpaRepository chapterJpaRepository;

    @Override
    public void save(Mission mission) {
        ChapterJpaEntity chapterEntity = chapterJpaRepository.getReferenceById(mission.getChapterId());
        missionJpaRepository.save(missionJpaMapper.toJpaEntity(mission, chapterEntity));
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

    @Override
    public List<Mission> findAllByChapterIdIn(List<Long> chapterIds) {
        return missionJpaRepository.findAllByChapterIdIn(chapterIds).stream().map(missionJpaMapper::toDomain).toList();
    }

    @Override
    public Optional<Mission> findByIdWithQuestions(Long id) {
        return missionJpaRepository.findByIdWithQuestions(id).map(missionJpaMapper::toDomain);
    }
}
