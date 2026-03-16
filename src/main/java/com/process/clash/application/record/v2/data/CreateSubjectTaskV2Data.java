package com.process.clash.application.record.v2.data;

import com.process.clash.application.common.actor.Actor;
import java.time.LocalDate;

public class CreateSubjectTaskV2Data {

    public record Command(
        Actor actor,
        Long subjectId,
        String name,
        LocalDate date
    ) {
    }
}
