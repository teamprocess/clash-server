package com.process.clash.application.mainpage.data.rival;

import com.process.clash.application.common.actor.Actor;

import java.util.List;

public class GetAllAbleRivalsData {

    public record Command(
            Actor actor
    ) {

        public static Command from(Actor actor) {

            return new Command(
                    actor
            );
        }
    }

    public record Result(
        List<User> users
    ) {

//        public static Result from() {
//
//        }
    }

    public record User(
            Long id,
            String name,
            String githubId
    ) {}
}
