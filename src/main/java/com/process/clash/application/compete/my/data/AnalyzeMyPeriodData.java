package com.process.clash.application.compete.my.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.common.enums.PeriodCategory;
import com.process.clash.domain.common.enums.TargetCategory;

import java.time.LocalDate;
import java.util.List;

public class AnalyzeMyPeriodData {

    public record Command(
            Actor actor,
            TargetCategory category,
            PeriodCategory period
    ) {

        public static Command of(Actor actor, TargetCategory category, PeriodCategory period) {
            return new Command(actor, category, period);
        }
    }

    public record Result(
            String category,
            String period,
            List<DataPoint> dataPoints
    ) {

        public static Result of(TargetCategory category, PeriodCategory period, List<DataPoint> dataPoints) {
            return new Result(
                    category.toString(),
                    period.toString(),
                    dataPoints
            );
        }
    }

    public record DataPoint(
            LocalDate date,
            Double point
    ) {}
}