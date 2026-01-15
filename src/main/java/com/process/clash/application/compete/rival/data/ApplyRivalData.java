package com.process.clash.application.compete.rival.data;

import com.process.clash.adapter.web.compete.rival.dto.ApplyRivalDto;
import com.process.clash.application.common.actor.Actor;

import java.util.List;

public class ApplyRivalData {

    public record Command(
            Actor actor,
            List<Id> ids
    ) {
        public static Command from(Actor actor, ApplyRivalDto.Request request) {
            return new Command(
                    actor,
                    request.ids().stream()
                            .map(dto -> new Id(
                                    dto.id()
                            ))
                    .toList()
            );
        }
    }

    public record Id(
            Long id
    ) {}
}