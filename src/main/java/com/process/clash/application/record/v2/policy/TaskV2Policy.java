package com.process.clash.application.record.v2.policy;

import com.process.clash.application.record.v2.exception.exception.forbidden.TaskV2AccessDeniedException;
import com.process.clash.domain.record.v2.entity.RecordTaskV2;
import org.springframework.stereotype.Component;

@Component
public class TaskV2Policy {

    public void validateBelongsToSubject(RecordTaskV2 task, Long subjectId) {
        if (!task.subjectId().equals(subjectId)) {
            throw new TaskV2AccessDeniedException();
        }
    }
}
