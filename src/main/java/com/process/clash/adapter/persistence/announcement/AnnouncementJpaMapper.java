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
}
