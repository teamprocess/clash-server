package com.process.clash.domain.rival.entity;

import com.process.clash.domain.rival.enums.RivalCurrentStatus;
import com.process.clash.domain.rival.enums.RivalLinkingStatus;

import java.time.LocalDateTime;

public record Rival(
        Long id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        RivalCurrentStatus rivalCurrentStatus,
        RivalLinkingStatus rivalLinkingStatus,
        Long myId,
        Long opponentId
) {

    public static Rival createDefault(Long myId, Long opponentId) {

        return new Rival(
                null,
                null,
                null,
                RivalCurrentStatus.OFFLINE,
                RivalLinkingStatus.PENDING,
                myId,
                opponentId
        );
    }
}
