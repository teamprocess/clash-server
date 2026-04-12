package com.process.clash.application.user.user.data;

import com.process.clash.application.common.actor.Actor;

public final class UpdateMyPrivacyData {

    private UpdateMyPrivacyData() {}

    public record Command(
        Actor actor,
        boolean isPrivate
    ) {}

    public record Result(
        boolean isPrivate
    ) {}
}
