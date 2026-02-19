package com.process.clash.application.record.policy;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.exception.exception.forbidden.TaskAccessDeniedException;
import com.process.clash.domain.record.entity.RecordTask;
import org.springframework.stereotype.Component;

@Component
public class TaskPolicy {

    public void validateOwnership(Actor actor, RecordTask task) {
        if (!task.user().id().equals(actor.id())) {
            throw new TaskAccessDeniedException();
        }
    }
}
