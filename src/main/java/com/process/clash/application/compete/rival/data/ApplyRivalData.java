package com.process.clash.application.compete.rival.data;

import com.process.clash.application.common.actor.Actor;

import java.util.List;

public class ApplyRivalData {

    public record Command(
            Actor actor,
            List<Id> ids
    ) {}

    public record Id(
            Long id
    ) {}
}