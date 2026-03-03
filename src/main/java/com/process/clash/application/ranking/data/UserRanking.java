package com.process.clash.application.ranking.data;

import com.process.clash.application.profile.data.EquippedItemsData;

public record UserRanking(
        Long userId,
        String name,
        String profileImage,
        Boolean isRival,
        String linkedId,
        Long point,
        EquippedItemsData equippedItems
) {
    public UserRanking(
            Long userId,
            String name,
            String profileImage,
            Boolean isRival,
            String linkedId,
            Long point
    ) {
        this(userId, name, profileImage, isRival, linkedId, point, EquippedItemsData.empty());
    }
}
