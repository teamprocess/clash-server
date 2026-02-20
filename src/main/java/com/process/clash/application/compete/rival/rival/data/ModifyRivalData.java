package com.process.clash.application.compete.rival.rival.data;

import com.process.clash.application.common.actor.Actor;

public class ModifyRivalData {

    public record Command(
            Actor actor,
            Long id
    ) {

        public static Command of(Actor actor, Long id) {
            return new Command(actor, id);
        }
    }
}
