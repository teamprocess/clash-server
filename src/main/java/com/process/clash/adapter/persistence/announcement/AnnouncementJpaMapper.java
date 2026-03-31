package com.process.clash.adapter.persistence.announcement;

import com.process.clash.domain.announcement.entity.Announcement;
import org.springframework.stereotype.Component;

@Component
public class AnnouncementJpaMapper {

    public Announcement toDomain(AnnouncementJpaEntity entity) {
        return new Announcement(
                entity.getId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getTitle(),
                entity.getAuthor(),
                entity.getUserId(),
                entity.getContent(),
                entity.getStartAt(),
                entity.getEndAt()
        );
    }

    public AnnouncementJpaEntity toJpaEntity(Announcement domain) {
        return new AnnouncementJpaEntity(
                domain.id(),
                domain.createdAt(),
                domain.updatedAt(),
                domain.title(),
                domain.author(),
                domain.userId(),
                domain.content(),
                domain.startAt(),
                domain.endAt()
        );
    }
}
