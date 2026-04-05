package com.process.clash.application.announcement.port.out;

import com.process.clash.domain.announcement.entity.Announcement;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface AnnouncementRepositoryPort {

    List<Announcement> findAllActive(Instant now);

    Announcement save(Announcement announcement);

    Optional<Announcement> findById(Long id);

    List<Announcement> findAll();

    void deleteById(Long id);
}
