package com.process.clash.adapter.web.announcement.admin.dto;

import com.process.clash.application.announcement.admin.data.GetAllAnnouncementsAdminData;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

public class GetAllAnnouncementsAdminDto {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private static String toKst(Instant instant) {
        return OffsetDateTime.ofInstant(instant, KST).toString();
    }

    @Schema(name = "GetAllAnnouncementsAdminDtoResponse")
    public record Response(List<AnnouncementItem> announcements) {

        public static Response from(GetAllAnnouncementsAdminData.Result result) {
            List<AnnouncementItem> items = result.announcements().stream()
                    .map(a -> new AnnouncementItem(
                            a.id(),
                            a.title(),
                            a.author(),
                            a.userId(),
                            a.content(),
                            toKst(a.startedAt()),
                            toKst(a.endedAt()),
                            toKst(a.createdAt())
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
            String endedAt,
            String createdAt
    ) {}
}
