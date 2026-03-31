package com.process.clash.adapter.web.announcement.admin.dto;

import com.process.clash.application.announcement.admin.data.UpdateAnnouncementAdminData;
import com.process.clash.application.common.actor.Actor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class UpdateAnnouncementAdminDto {

    @Schema(name = "UpdateAnnouncementAdminDtoRequest")
    public record Request(
            @NotBlank(message = "제목은 비워둘 수 없습니다.")
            String title,

            @NotBlank(message = "작성자는 비워둘 수 없습니다.")
            String author,

            @NotBlank(message = "내용은 비워둘 수 없습니다.")
            String content,

            @NotNull(message = "시작 시각은 비워둘 수 없습니다.")
            Instant startAt,

            @NotNull(message = "종료 시각은 비워둘 수 없습니다.")
            Instant endAt
    ) {
        public UpdateAnnouncementAdminData.Command toCommand(Actor actor, Long id) {
            return new UpdateAnnouncementAdminData.Command(actor, id, title, author, content, startAt, endAt);
        }
    }

    @Schema(name = "UpdateAnnouncementAdminDtoResponse")
    public record Response(
            Long id,
            String title,
            String author,
            Long userId,
            String content,
            String startAt,
            String endAt,
            String updatedAt
    ) {
        public static Response from(UpdateAnnouncementAdminData.Result result) {
            return new Response(
                    result.id(),
                    result.title(),
                    result.author(),
                    result.userId(),
                    result.content(),
                    result.startAt().toString(),
                    result.endAt().toString(),
                    result.updatedAt() != null ? result.updatedAt().toString() : null
            );
        }
    }
}
