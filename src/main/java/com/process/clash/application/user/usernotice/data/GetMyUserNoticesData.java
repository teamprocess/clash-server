package com.process.clash.application.user.usernotice.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.user.usernotice.entity.UserNotice;
import com.process.clash.domain.user.usernotice.enums.NoticeCategory;

import java.time.Instant;
import java.util.List;

public class GetMyUserNoticesData {

    public record Command(Actor actor) {

        public static Command from(Actor actor) {
            return new Command(actor);
        }
    }

    public record Result(List<NoticeItem> notices) {}

    public record NoticeItem(
            Long id,
            NoticeCategory category,
            boolean isRead,
            Long senderId,
            Instant createdAt
    ) {
        public static NoticeItem from(UserNotice notice) {
            return new NoticeItem(
                    notice.id(),
                    notice.noticeCategory(),
                    notice.isRead(),
                    notice.senderId(),
                    notice.createdAt()
            );
        }
    }
}
