package com.process.clash.application.compete.my.data;

import com.process.clash.application.common.actor.Actor;

import java.util.List;

public class CompareMyActivityData {

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
            Double earnedExp,
            Double studyTime,
            Double gitHubAttribution,
            Double commitCount
    ) {

        public static Result from(List<Double> result) {

            return new Result(
                    round(result.get(0)), // earnedExp
                    round(result.get(1)), // studyTime
                    round(result.get(2)), // gitHubAttribution
                    round(result.get(3))  // commitCount
            );
        }

        private static double round(Double value) {
            if (value == null) return 0.0;
            return Math.round(value * 100.0) / 100.0;
        }
    }
}
