package com.process.clash.adapter.web.announcement.dto;

import com.process.clash.application.announcement.data.GetActiveAnnouncementsData;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

public class GetActiveAnnouncementsDto {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private static String toKst(Instant instant) {
        return instant != null ? OffsetDateTime.ofInstant(instant, KST).toString() : null;
    }

    public record Response(List<AnnouncementItem> announcements) {

        public static Response from(GetActiveAnnouncementsData.Result result) {
            List<AnnouncementItem> items = result.announcements().stream()
                    .map(announcement -> new AnnouncementItem(
                            announcement.id(),
                            announcement.title(),
                            announcement.author(),
                            announcement.userId(),
                            announcement.content(),
                            toKst(announcement.startedAt()),
                            toKst(announcement.endedAt())
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
            String startedAt,
            String endedAt
    ) {}
}
