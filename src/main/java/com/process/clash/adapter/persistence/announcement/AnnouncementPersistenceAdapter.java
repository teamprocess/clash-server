package com.process.clash.adapter.persistence.announcement;

import com.process.clash.application.announcement.port.out.AnnouncementRepositoryPort;
import com.process.clash.domain.announcement.entity.Announcement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AnnouncementPersistenceAdapter implements AnnouncementRepositoryPort {

    private final AnnouncementJpaRepository announcementJpaRepository;
    private final AnnouncementJpaMapper announcementJpaMapper;

    @Override
    public List<Announcement> findAllActive(Instant now) {
        return announcementJpaRepository.findAllActive(now)
                .stream()
                .map(announcementJpaMapper::toDomain)
                .toList();
    }

    @Override
    public Announcement save(Announcement announcement) {
        AnnouncementJpaEntity entity = announcementJpaMapper.toJpaEntity(announcement);
        return announcementJpaMapper.toDomain(announcementJpaRepository.save(entity));
    }

    @Override
    public Optional<Announcement> findById(Long id) {
        return announcementJpaRepository.findById(id)
                .map(announcementJpaMapper::toDomain);
    }

    @Override
    public List<Announcement> findAll() {
        return announcementJpaRepository.findAll()
                .stream()
                .map(announcementJpaMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        announcementJpaRepository.deleteById(id);
    }
}
