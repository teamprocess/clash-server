package com.process.clash.adapter.web.user.dto;

import com.process.clash.application.profile.data.GetUserProfileData;
import com.process.clash.application.realtime.data.UserActivityStatus;
import com.process.clash.domain.common.enums.Major;

public class GetUserProfileDto {

    public record Response(
        Long id,
        String username,
        String name,
        String profileImage,
        Major major,
        String tier,
        UserActivityStatus activityStatus,
        boolean isPrivate,
        EquippedItemsDto.Response equippedItems
    ) {
        public static Response from(GetUserProfileData.Result result) {
            return new Response(
                result.id(),
                result.username(),
                result.name(),
                result.profileImage(),
                result.major(),
                result.tier(),
                result.activityStatus(),
                result.isPrivate(),
                EquippedItemsDto.Response.from(result.equippedItems())
            );
        }
    }
}
