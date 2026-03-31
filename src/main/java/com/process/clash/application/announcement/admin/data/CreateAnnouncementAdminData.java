package com.process.clash.application.announcement.admin.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.announcement.entity.Announcement;

import java.time.Instant;

public class CreateAnnouncementAdminData {

    public record Command(
            Actor actor,
            String title,
            String author,
            String content,
            Instant startAt,
            Instant endAt
    ) {
        public Announcement toDomain() {
            return new Announcement(null, null, null, title, author, actor.id(), content, startAt, endAt);
        }
    }

    public record Result(
            Long id,
            String title,
            String author,
            Long userId,
            String content,
            Instant startAt,
            Instant endAt,
            Instant createdAt
    ) {
        public static Result from(Announcement announcement) {
            return new Result(
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
