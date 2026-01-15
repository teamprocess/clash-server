package com.process.clash.adapter.persistence.roadmap.section;

import com.process.clash.application.roadmap.section.port.out.SectionRepositoryPort;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.roadmap.entity.Section;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SectionPersistenceAdapter implements SectionRepositoryPort {
    private final SectionJpaRepository sectionJpaRepository;
    private final SectionJpaMapper sectionJpaMapper;

    @Override
    public Section save(Section section) {
        SectionJpaEntity entity = sectionJpaMapper.toJpaEntity(section);
        SectionJpaEntity saved = sectionJpaRepository.save(entity);
        return sectionJpaMapper.toDomain(saved);
    }

    @Override
    public Optional<Section> findById(Long id) {
        return sectionJpaRepository.findById(id).map(sectionJpaMapper::toDomain);
    }

    @Override
    public List<Section> findAll() {
        return sectionJpaRepository.findAll().stream().map(sectionJpaMapper::toDomain).toList();
    }

    @Override
    public List<Section> findAllByMajor(Major major) {
        return sectionJpaRepository.findAllByMajor(major).stream().map(sectionJpaMapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        sectionJpaRepository.deleteById(id);
    }
}
