package com.process.clash.application.announcement.service;

import com.process.clash.application.announcement.data.GetActiveAnnouncementsData;
import com.process.clash.application.announcement.port.in.GetActiveAnnouncementsUseCase;
import com.process.clash.application.announcement.port.out.AnnouncementRepositoryPort;
import com.process.clash.domain.announcement.entity.Announcement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetActiveAnnouncementsService implements GetActiveAnnouncementsUseCase {

    private final AnnouncementRepositoryPort announcementRepositoryPort;

    @Override
    public GetActiveAnnouncementsData.Result execute() {
        Instant now = Instant.now();
        List<Announcement> announcements = announcementRepositoryPort.findAllActive(now);
        List<GetActiveAnnouncementsData.AnnouncementItem> items = announcements.stream()
                .map(GetActiveAnnouncementsData.AnnouncementItem::from)
                .toList();
        return new GetActiveAnnouncementsData.Result(items);
    }
}
