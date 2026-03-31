package com.process.clash.adapter.web.announcement.admin.dto;

import com.process.clash.application.announcement.admin.data.GetAllAnnouncementsAdminData;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class GetAllAnnouncementsAdminDto {

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
                            a.startAt().toString(),
                            a.endAt().toString(),
                            a.createdAt().toString()
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
            String endAt,
            String createdAt
    ) {}
}
