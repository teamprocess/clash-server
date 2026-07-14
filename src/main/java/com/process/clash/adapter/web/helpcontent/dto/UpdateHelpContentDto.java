package com.process.clash.adapter.web.helpcontent.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.helpcontent.data.UpdateHelpContentData;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public class UpdateHelpContentDto {

    @Schema(name = "UpdateHelpContentDtoRequest")
    public record Request(
            @NotBlank(message = "도움말 내용은 비워둘 수 없습니다.")
            String content
    ) {
        public UpdateHelpContentData.Command toCommand(Actor actor, String key) {
            return new UpdateHelpContentData.Command(actor, key, content);
        }
    }

    @Schema(name = "UpdateHelpContentDtoResponse")
    public record Response(String key, String content, long version, Instant updatedAt) {
        public static Response from(UpdateHelpContentData.Result result) {
            return new Response(result.key(), result.content(), result.version(), result.updatedAt());
        }
    }
}
