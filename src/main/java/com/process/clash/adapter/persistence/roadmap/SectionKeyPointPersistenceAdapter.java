package com.process.clash.adapter.persistence.roadmap;

import com.process.clash.application.roadmap.port.out.SectionKeyPointRepositoryPort;
import com.process.clash.domain.roadmap.SectionKeyPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SectionKeyPointPersistenceAdapter implements SectionKeyPointRepositoryPort {

    private final SectionKeyPointJpaRepository sectionKeyPointJpaRepository;
    private final SectionKeyPointJpaMapper sectionKeyPointJpaMapper;

    @Override
    public void save(SectionKeyPoint keyPoint) {
        sectionKeyPointJpaRepository.save(sectionKeyPointJpaMapper.toJpaEntity(keyPoint));
    }

    @Override
    public Optional<SectionKeyPoint> findById(Long id) {
        return sectionKeyPointJpaRepository.findById(id).map(sectionKeyPointJpaMapper::toDomain);
    }

    @Override
    public List<SectionKeyPoint> findAll() {
        return sectionKeyPointJpaRepository.findAll().stream().map(sectionKeyPointJpaMapper::toDomain).toList();
    }

    @Override
    public List<SectionKeyPoint> findAllBySectionId(Long sectionId) {
        return sectionKeyPointJpaRepository.findAllBySectionId(sectionId).stream().map(sectionKeyPointJpaMapper::toDomain).toList();
    }
}
