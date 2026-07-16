package com.process.clash.adapter.web.helpcontent.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.helpcontent.data.CreateHelpContentData;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public class CreateHelpContentDto {

    @Schema(name = "CreateHelpContentDtoRequest")
    public record Request(
            @NotBlank(message = "도움말 키는 비워둘 수 없습니다.")
            @Size(max = 100, message = "도움말 키는 100자 이하이어야 합니다.")
            @Pattern(regexp = "[a-z0-9-]+", message = "도움말 키는 영문 소문자, 숫자, 하이픈만 사용할 수 있습니다.")
            String key,

            @NotBlank(message = "도움말 내용은 비워둘 수 없습니다.")
            String content
    ) {
        public CreateHelpContentData.Command toCommand(Actor actor) {
            return new CreateHelpContentData.Command(actor, key, content);
        }
    }

    @Schema(name = "CreateHelpContentDtoResponse")
    public record Response(String key, String content, long version, Instant createdAt) {
        public static Response from(CreateHelpContentData.Result result) {
            return new Response(result.key(), result.content(), result.version(), result.createdAt());
        }
    }
}
