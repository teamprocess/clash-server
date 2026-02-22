package com.process.clash.adapter.web.user.dto;

import com.process.clash.application.user.usernotice.data.GetMyUserNoticesData;

import java.util.List;

public class GetMyUserNoticesDto {

    public record Response(List<NoticeItem> notices) {

        public static Response from(GetMyUserNoticesData.Result result) {
            List<NoticeItem> items = result.notices().stream()
                    .map(notice -> new NoticeItem(
                            notice.id(),
                            notice.category().name(),
                            notice.category().getMessage(),
                            notice.category().requiresAction(),
                            notice.isRead(),
                            notice.senderId(),
                            notice.createdAt() != null ? notice.createdAt().toString() : null
                    ))
                    .toList();
            return new Response(items);
        }
    }

    private record NoticeItem(
            Long id,
            String category,
            String message,
            boolean requiresAction,
            boolean isRead,
            Long senderId,
            String createdAt
    ) {}
}
