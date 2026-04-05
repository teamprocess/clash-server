package com.process.clash.adapter.web.announcement.admin.dto;

import com.process.clash.application.announcement.admin.data.CreateAnnouncementAdminData;
import com.process.clash.application.common.actor.Actor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class CreateAnnouncementAdminDto {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private static String toKst(Instant instant) {
        return OffsetDateTime.ofInstant(instant, KST).toString();
    }

    @Schema(name = "CreateAnnouncementAdminDtoRequest")
    public record Request(
            @NotBlank(message = "제목은 비워둘 수 없습니다.")
            String title,

            String author,

            @NotBlank(message = "내용은 비워둘 수 없습니다.")
            String content,

            @NotNull(message = "시작 시각은 비워둘 수 없습니다.")
            LocalDateTime startedAt,

            @NotNull(message = "종료 시각은 비워둘 수 없습니다.")
            LocalDateTime endedAt
    ) {
        public CreateAnnouncementAdminData.Command toCommand(Actor actor) {
            return new CreateAnnouncementAdminData.Command(
                    actor, title, author != null ? author : "PROCESS", content,
                    startedAt.atZone(KST).toInstant(),
                    endedAt.atZone(KST).toInstant()
            );
        }
    }

    @Schema(name = "CreateAnnouncementAdminDtoResponse")
    public record Response(
            Long id,
            String title,
            String author,
            Long userId,
            String content,
            String startedAt,
            String endedAt,
            String createdAt
    ) {
        public static Response from(CreateAnnouncementAdminData.Result result) {
            return new Response(
                    result.id(),
                    result.title(),
                    result.author(),
                    result.userId(),
                    result.content(),
                    toKst(result.startedAt()),
                    toKst(result.endedAt()),
                    toKst(result.createdAt())
            );
        }
    }
}
