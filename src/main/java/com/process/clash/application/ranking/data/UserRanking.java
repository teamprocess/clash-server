package com.process.clash.application.ranking.data;

public record UserRanking(
        Long userId,
        String name,
        String profileImage,
        Boolean isRival,
        String linkedId,
        Long point
) {}