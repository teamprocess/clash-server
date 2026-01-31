package com.process.clash.application.profile.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.common.enums.Major;
import com.process.clash.domain.user.user.entity.User;
import com.process.clash.domain.user.user.enums.Role;
import com.process.clash.domain.user.user.enums.UserStatus;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class GetMyProfileData {

    public record Command(
        Actor actor
    ) {}

    public record Result(
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
        int totalToken,
        Major major,
        UserStatus userStatus,
        boolean githubLinked
    ) {
        public static Result from(User user, boolean githubLinked) {
            return new Result(
                user.id(),
                user.createdAt().atZone(ZoneId.of("Asia/Seoul")).toInstant(),
                user.updatedAt().atZone(ZoneId.of("Asia/Seoul")).toInstant(),
                user.username(),
                user.name(),
                user.email(),
                user.role(),
                user.profileImage(),
                user.totalExp(),
                user.totalCookie(),
                user.totalToken(),
                user.major(),
                user.userStatus(),
                githubLinked
            );
        }
    }
}
