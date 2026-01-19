package com.process.clash.adapter.web.compete.rival.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.data.ApplyRivalData;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

public class ApplyRivalDto {

    @Schema(name = "ApplyRivalDtoRequest")

    public record Request(
            @NotEmpty(message = "아이디 목록은 비워둘 수 없습니다.")
            List<Id> ids
    ) {
        public ApplyRivalData.Command toCommand(Actor actor) {
            return new ApplyRivalData.Command(
                    actor,
                    ids.stream()
                            .map(dto -> new ApplyRivalData.Id(dto.id()))
                            .toList()
            );
        }
    }

    public record Id(
            Long id
    ) {}
}
