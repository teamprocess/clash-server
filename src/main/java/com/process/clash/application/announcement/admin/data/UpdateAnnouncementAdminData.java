package com.process.clash.application.announcement.admin.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.announcement.entity.Announcement;

import java.time.Instant;

public class UpdateAnnouncementAdminData {

    public record Command(
            Actor actor,
            Long id,
            String title,
            String author,
            String content,
            Instant startedAt,
            Instant endedAt
    ) {}

    public record Result(
            Long id,
            String title,
            String author,
            Long userId,
            String content,
            Instant startedAt,
            Instant endedAt,
            Instant updatedAt
    ) {
        public static Result from(Announcement announcement) {
            return new Result(
                    announcement.id(),
                    announcement.title(),
                    announcement.author(),
                    announcement.userId(),
                    announcement.content(),
                    announcement.startedAt(),
                    announcement.endedAt(),
                    announcement.updatedAt()
            );
        }
    }
}
