package com.process.clash.application.helpcontent.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.helpcontent.entity.HelpContent;

import java.time.Instant;

public class CreateHelpContentData {

    public record Command(Actor actor, String key, String content) {
        public HelpContent toDomain() {
            return new HelpContent(key, content, 1, null, null);
        }
    }

    public record Result(String key, String content, long version, Instant createdAt) {
        public static Result from(HelpContent helpContent) {
            return new Result(
                    helpContent.key(),
                    helpContent.content(),
                    helpContent.version(),
                    helpContent.createdAt()
            );
        }
    }
}
