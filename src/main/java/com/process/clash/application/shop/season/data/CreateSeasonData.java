package com.process.clash.application.shop.season.data;

import com.process.clash.application.common.actor.Actor;

import java.time.LocalDate;

public class CreateSeasonData {

    public record Command(
            Actor actor,
            String title,
            LocalDate startDate,
            LocalDate endDate
    ) {}
}
