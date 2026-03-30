package com.process.clash.adapter.web.announcement.dto;

import com.process.clash.application.announcement.data.GetActiveAnnouncementsData;

import java.util.List;

public class GetActiveAnnouncementsDto {

    public record Response(List<AnnouncementItem> announcements) {

        public static Response from(GetActiveAnnouncementsData.Result result) {
            List<AnnouncementItem> items = result.announcements().stream()
                    .map(announcement -> new AnnouncementItem(
                            announcement.id(),
                            announcement.title(),
                            announcement.author(),
                            announcement.userId(),
                            announcement.content(),
                            announcement.startAt() != null ? announcement.startAt().toString() : null,
                            announcement.endAt() != null ? announcement.endAt().toString() : null
                    ))
                    .toList();
            return new Response(items);
        }
    }

    public record AnnouncementItem(
            Long id,
            String title,
            String author,
            Long userId,
            String content,
            String startAt,
            String endAt
    ) {}
}
