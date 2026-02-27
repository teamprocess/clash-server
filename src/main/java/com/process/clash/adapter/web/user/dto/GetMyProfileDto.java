package com.process.clash.adapter.web.user.dto;

import com.process.clash.application.profile.data.GetMyProfileData;
import com.process.clash.application.realtime.data.UserActivityStatus;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import java.time.Instant;

public class GetMyProfileDto {

    public record Response(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String name,
        String email,
        Role role,
        String profileImage,
        int totalExp,
        int totalCookie,
        Major major,
        UserStatus userStatus,
        boolean githubLinked,
        UserActivityStatus activityStatus
    ) {
        public static GetMyProfileDto.Response from(GetMyProfileData.Result result) {
            return new Response(
              result.id(),
              result.createdAt(),
              result.updatedAt(),
              result.username(),
              result.name(),
              result.email(),
              result.role(),
              result.profileImage(),
              result.totalExp(),
              result.totalCookie(),
              result.major(),
              result.userStatus(),
              result.githubLinked(),
              result.activityStatus()
            );
        }
    }
}
