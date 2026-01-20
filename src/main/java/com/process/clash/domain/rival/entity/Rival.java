package com.process.clash.domain.rival.entity;

import com.process.clash.domain.rival.enums.RivalLinkingStatus;

import java.time.LocalDateTime;

public record Rival(
        Long id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        RivalLinkingStatus rivalLinkingStatus,
        Long firstUserId,
        Long secondUserId
) {

    public static Rival createDefault(Long firstUserId, Long secondUser2Id) {

        return new Rival(
                null,
                null,
                null,
                RivalLinkingStatus.PENDING,
                firstUserId,
                secondUser2Id
        );
    }

    public Rival accept() {

        return new Rival(
                this.id,
                this.createdAt,
                LocalDateTime.now(),
                RivalLinkingStatus.ACCEPTED,
                this.firstUserId,
                this.secondUserId
        );
    }

    public Rival reject() {

        return new Rival(
                this.id,
                this.createdAt,
                LocalDateTime.now(),
                RivalLinkingStatus.REJECTED,
                this.firstUserId,
                this.secondUserId
        );
    }
}
