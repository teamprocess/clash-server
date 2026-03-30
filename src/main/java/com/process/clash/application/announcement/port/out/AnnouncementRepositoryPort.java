package com.process.clash.application.announcement.port.out;

import com.process.clash.domain.announcement.entity.Announcement;

import java.time.Instant;
import java.util.List;

public interface AnnouncementRepositoryPort {

    List<Announcement> findAllActive(Instant now);
}
