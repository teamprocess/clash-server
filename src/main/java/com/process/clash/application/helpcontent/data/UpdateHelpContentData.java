package com.process.clash.application.helpcontent.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.helpcontent.entity.HelpContent;

import java.time.Instant;

public class UpdateHelpContentData {

    public record Command(Actor actor, String key, String content) {
    }

    public record Result(String key, String content, long version, Instant updatedAt) {
        public static Result from(HelpContent helpContent) {
            return new Result(
                    helpContent.key(),
                    helpContent.content(),
                    helpContent.version(),
                    helpContent.updatedAt()
            );
        }
    }
}
