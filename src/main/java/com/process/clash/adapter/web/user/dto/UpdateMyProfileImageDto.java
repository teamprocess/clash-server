package com.process.clash.adapter.web.user.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.user.user.data.UpdateMyProfileImageData;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UpdateMyProfileImageDto {

    @Schema(name = "UpdateMyProfileImageRequest")
    public record Request(
            @NotBlank(message = "profileImageUrl은 필수 입력값입니다.")
            @Pattern(
                    regexp = "^https?://.+$",
                    message = "profileImageUrl은 http(s) URL 형식이어야 합니다."
            )
            String profileImageUrl
    ) {
        public UpdateMyProfileImageData.Command toCommand(Actor actor) {
            return new UpdateMyProfileImageData.Command(actor, profileImageUrl);
        }
    }

    public record Response(
            String profileImageUrl
    ) {
        public static Response from(UpdateMyProfileImageData.Result result) {
            return new Response(result.profileImageUrl());
        }
    }
}
