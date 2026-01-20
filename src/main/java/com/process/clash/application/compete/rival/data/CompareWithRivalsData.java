package com.process.clash.application.compete.rival.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.common.enums.PeriodCategory;
import com.process.clash.domain.roadmap.entity.Category;

import java.time.LocalDate;
import java.util.List;

public class CompareWithRivalsData {

    public record Command(
            Actor actor,
            Category category,
            PeriodCategory period
    ) {

        public static Command from(Actor actor, Category category, PeriodCategory period) {

            return new Command(actor, category, period);
        }
    }

    public record Result(
            String category,
            String period,
            List<TotalData> totalData
    ) {}

    public record TotalData(
            Long id,
            String name,
            List<DataPoint> dataPoint
    ) {}

    public record DataPoint(
            LocalDate date,
            Double point
    ) {}
}

