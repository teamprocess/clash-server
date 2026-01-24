package com.process.clash.application.compete.my.data;

import com.process.clash.application.common.actor.Actor;

import java.util.List;

public class GetMyGrowthRateData {

    public record Command(
            Actor actor,
            String category
    ) {

        public static Command of(Actor actor, String category) {

            return new Command(
                    actor,
                    category
            );
        }
    }

    public record Result(
            List<DataPoint> dataPoint
    ) {

        public static Result from(List<DataPoint> dataPoint) {

            return new Result(
                    dataPoint
            );
        }
    }
}
