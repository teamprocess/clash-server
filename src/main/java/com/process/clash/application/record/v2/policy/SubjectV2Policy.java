package com.process.clash.application.record.v2.policy;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.v2.exception.exception.forbidden.SubjectV2AccessDeniedException;
import com.process.clash.domain.record.v2.entity.RecordSubjectV2;
import org.springframework.stereotype.Component;

@Component
public class SubjectV2Policy {

    public void validateOwnership(Actor actor, RecordSubjectV2 subject) {
        if (!actor.id().equals(subject.userId())) {
            throw new SubjectV2AccessDeniedException();
        }
    }
}
