package com.process.clash.application.ranking.data;

import com.process.clash.application.profile.data.EquippedItemsData;
import com.process.clash.domain.user.userrankhistory.enums.ExpTier;
import com.process.clash.domain.user.userrankhistory.enums.RankTier;

public record UserRanking(
        Long userId,
        String name,
        String profileImage,
        Boolean isRival,
        String linkedId,
        Long point,
        RankTier rankTier,
        ExpTier expTier,
        EquippedItemsData equippedItems
) {
    public UserRanking(
            Long userId,
            String name,
            String profileImage,
            Boolean isRival,
            String linkedId,
            Long point,
            RankTier rankTier,
            ExpTier expTier
    ) {
        this(userId, name, profileImage, isRival, linkedId, point, rankTier, expTier, EquippedItemsData.empty());
    }

    public UserRanking(
            Long userId,
            String name,
            String profileImage,
            Boolean isRival,
            String linkedId,
            Long point
    ) {
        this(userId, name, profileImage, isRival, linkedId, point, RankTier.NONE, ExpTier.UNRANKED, EquippedItemsData.empty());
    }
}
