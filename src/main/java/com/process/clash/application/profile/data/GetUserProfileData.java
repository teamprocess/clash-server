package com.process.clash.application.profile.data;

import com.process.clash.application.realtime.data.UserActivityStatus;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.user.user.entity.User;

public class GetUserProfileData {

    public record Command(
        Long targetUserId
    ) {}

    public record Result(
        Long id,
        String username,
        String name,
        String profileImage,
        Major major,
        String tier,
        UserActivityStatus activityStatus,
        boolean isPrivate,
        EquippedItemsData equippedItems
    ) {
        public static Result from(
            User user,
            UserActivityStatus activityStatus,
            EquippedItemsData equippedItems
        ) {
            return new Result(
                user.id(),
                user.username(),
                user.name(),
                user.profileImage(),
                user.major(),
                user.tier(),
                activityStatus,
                user.isPrivate(),
                equippedItems
            );
        }
    }
}
