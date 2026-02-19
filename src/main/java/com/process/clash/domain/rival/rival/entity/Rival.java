package com.process.clash.domain.rival.rival.entity;

import com.process.clash.domain.rival.rival.enums.RivalLinkingStatus;

import java.time.Instant;

public record Rival(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        RivalLinkingStatus rivalLinkingStatus,
        Long firstUserId,
        Long secondUserId
) {

    public static Rival createDefault(Long firstUserId, Long secondUserId) {

        return new Rival(
                null,
                null,
                null,
                RivalLinkingStatus.PENDING,
                firstUserId,
                secondUserId
        );
    }

    public Rival accept() {

        return new Rival(
                this.id,
                this.createdAt,
                Instant.now(),
                RivalLinkingStatus.ACCEPTED,
                this.firstUserId,
                this.secondUserId
        );
    }

    public Rival reject() {

        return new Rival(
                this.id,
                this.createdAt,
                Instant.now(),
                RivalLinkingStatus.REJECTED,
                this.firstUserId,
                this.secondUserId
        );
    }
}
