package com.process.clash.application.announcement.data;

import com.process.clash.domain.announcement.entity.Announcement;

import java.time.Instant;
import java.util.List;

public class GetActiveAnnouncementsData {

    public record Result(List<AnnouncementItem> announcements) {}

    public record AnnouncementItem(
            Long id,
            String title,
            String author,
            Long userId,
            String content,
            Instant startedAt,
            Instant endedAt
    ) {
        public static AnnouncementItem from(Announcement announcement) {
            return new AnnouncementItem(
                    announcement.id(),
                    announcement.title(),
                    announcement.author(),
                    announcement.userId(),
                    announcement.content(),
                    announcement.startedAt(),
                    announcement.endedAt()
            );
        }
    }
}
