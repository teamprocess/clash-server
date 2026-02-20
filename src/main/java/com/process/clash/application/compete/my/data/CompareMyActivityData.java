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
                    result.get(0), // earnedExp
                    result.get(1), // studyTime
                    result.get(2), // gitHubAttribution
                    result.get(3)  // commitCount
            );
        }
    }
}
