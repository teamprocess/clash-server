package com.process.clash.adapter.persistence.announcement;

import com.process.clash.application.announcement.port.out.AnnouncementRepositoryPort;
import com.process.clash.domain.announcement.entity.Announcement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

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
}
