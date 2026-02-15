package com.process.clash.application.user.user.data;

import com.process.clash.application.common.actor.Actor;

public final class UpdateMyProfileImageData {

    private UpdateMyProfileImageData() {
    }

    public record Command(
            Actor actor,
            String profileImageUrl
    ) {
    }

    public record Result(
            String profileImageUrl
    ) {
    }
}
