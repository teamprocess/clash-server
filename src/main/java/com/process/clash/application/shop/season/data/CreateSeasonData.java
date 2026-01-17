package com.process.clash.application.shop.season.data;

import com.process.clash.adapter.web.shop.season.dto.CreateSeasonDto;
import com.process.clash.application.common.actor.Actor;

import java.time.LocalDate;

public class CreateSeasonData {

    public record Command(
            Actor actor,
            String title,
            LocalDate startDate,
            LocalDate endDate
    ) {
        public static CreateSeasonData.Command from(CreateSeasonDto.Request request, Actor actor) {
            return new CreateSeasonData.Command(
                    actor,
                    request.title(),
                    request.startDate(),
                    request.endDate()
            );
        }
    }
}
