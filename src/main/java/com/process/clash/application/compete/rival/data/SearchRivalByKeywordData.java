package com.process.clash.application.compete.rival.data;

import com.process.clash.application.common.actor.Actor;

import java.util.List;

public class SearchRivalByKeywordData {

    public record Command(
            Actor actor,
            String keyword
    ) {

        public static Command from(Actor actor, String keyword) {

            return new Command(
                    actor,
                    keyword
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
