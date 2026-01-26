package com.process.clash.application.group.data;

import com.process.clash.application.common.actor.Actor;

public class QuitGroupData {

    public record Command(
        Actor actor,
        Long groupId
    ) {

        public static Command of(Actor actor, Long groupId) {
            return new Command(actor, groupId);
        }
    }
}
