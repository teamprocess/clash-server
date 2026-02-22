package com.process.clash.application.user.usernotice.data;

import com.process.clash.application.common.actor.Actor;

public class ReadUserNoticeData {

    public record Command(Actor actor, Long noticeId) {

        public static Command of(Actor actor, Long noticeId) {
            return new Command(actor, noticeId);
        }
    }
}
