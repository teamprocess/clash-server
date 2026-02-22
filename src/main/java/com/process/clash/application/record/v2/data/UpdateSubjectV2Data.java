package com.process.clash.application.record.v2.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.record.v2.entity.RecordSubjectV2;

public class UpdateSubjectV2Data {

    public record Command(
        Actor actor,
        Long subjectId,
        String name
    ) {
    }

    public record Result(
        RecordSubjectV2 subject
    ) {
        public static Result from(RecordSubjectV2 subject) {
            return new Result(subject);
        }
    }
}
