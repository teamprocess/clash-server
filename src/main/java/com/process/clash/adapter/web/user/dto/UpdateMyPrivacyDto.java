package com.process.clash.adapter.web.user.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.user.user.data.UpdateMyPrivacyData;
import jakarta.validation.constraints.NotNull;

public class UpdateMyPrivacyDto {

    public record Request(
        @NotNull Boolean isPrivate
    ) {
        public UpdateMyPrivacyData.Command toCommand(Actor actor) {
            return new UpdateMyPrivacyData.Command(actor, isPrivate);
        }
    }

    public record Response(
        boolean isPrivate
    ) {
        public static Response from(UpdateMyPrivacyData.Result result) {
            return new Response(result.isPrivate());
        }
    }
}
