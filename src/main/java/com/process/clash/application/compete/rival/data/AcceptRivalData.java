package com.process.clash.application.compete.rival.data;

import com.process.clash.application.common.actor.Actor;

public class AcceptRivalData {

    public record Command(
            Actor actor,
            Long id
    ) {}
}
