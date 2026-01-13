package com.process.clash.application.mainpage.data;

import com.process.clash.application.common.actor.Actor;
import lombok.Builder;

public class GetUserProfileData {
    @Builder
    public record Command(
            Actor actor
    ) {}

    @Builder
    public record Result(
            Long id,
            String name,
            String username,
            String profileImage
    ) {}
}
