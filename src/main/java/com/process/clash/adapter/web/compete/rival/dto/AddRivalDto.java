package com.process.clash.adapter.web.compete.rival.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.data.AddRivalData;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class AddRivalDto {

    public record Request(
            @NotEmpty(message = "아이디 목록은 비워둘 수 없습니다.")
            List<Id> ids
    ) {
        public AddRivalData.Command toCommand(Actor actor) {
            return new AddRivalData.Command(
                    actor,
                    ids.stream()
                            .map(dto -> new AddRivalData.Id(dto.id()))
                            .toList()
            );
        }
    }

    public record Id(
            Long id
    ) {}
}
