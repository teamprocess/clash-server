package com.process.clash.application.record.dto;

import com.process.clash.application.common.actor.Actor;
import java.time.Instant;

public class StartRecordData {

    public record Command(
        Long taskId,
        Actor actor
    ) {}

    public record Result(
        Instant startedAt
    ) {}
}
