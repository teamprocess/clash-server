package com.process.clash.application.announcement.admin.data;

import com.process.clash.domain.announcement.entity.Announcement;

import java.time.Instant;
import java.util.List;

public class GetAllAnnouncementsAdminData {

    public record Result(List<AnnouncementItem> announcements) {}

    public record AnnouncementItem(
            Long id,
            String title,
            String author,
            Long userId,
            String content,
            Instant startAt,
            Instant endAt,
            Instant createdAt
    ) {
        public static AnnouncementItem from(Announcement announcement) {
            return new AnnouncementItem(
                    announcement.id(),
                    announcement.title(),
                    announcement.author(),
                    announcement.userId(),
                    announcement.content(),
                    announcement.startAt(),
                    announcement.endAt(),
                    announcement.createdAt()
            );
        }
    }
}
