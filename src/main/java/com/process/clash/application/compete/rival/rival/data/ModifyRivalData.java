package com.process.clash.application.compete.rival.rival.data;

import com.process.clash.application.common.actor.Actor;

public class ModifyRivalData {

    public record Command(
            Actor actor,
            Long id
    ) {}
}
