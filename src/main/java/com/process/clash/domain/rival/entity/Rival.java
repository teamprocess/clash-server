package com.process.clash.domain.rival.entity;

import com.process.clash.domain.rival.enums.RivalStatus;

import java.time.LocalDateTime;

public record Rival(
        Long id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        RivalStatus rivalStatus,
        Long myId,
        Long opponentId
) {

    public static Rival createDefault(RivalStatus rivalStatus, Long myId, Long opponentId) {

        return new Rival(
                null,
                null,
                null,
                RivalStatus.OFFLINE,
                myId,
                opponentId
        );
    }
}
