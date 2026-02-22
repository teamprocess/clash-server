package com.process.clash.application.record.v2.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.record.v2.entity.RecordTaskV2;

public class UpdateSubjectTaskV2Data {

    public record Command(
        Actor actor,
        Long subjectId,
        Long taskId,
        String name
    ) {
    }

    public record Result(
        RecordTaskV2 task
    ) {
        public static Result from(RecordTaskV2 task) {
            return new Result(task);
        }
    }
}
