package com.process.clash.adapter.web.group.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.group.data.UpdateGroupData;
import com.process.clash.domain.group.enums.GroupCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateGroupDto {

    public record Request(
        @NotBlank(message = "name은 필수 입력값입니다.")
        String name,

        @NotBlank(message = "description은 필수 입력값입니다.")
        String description,

        @NotNull(message = "maxMembers는 필수 입력값입니다.")
        @Min(value = 1, message = "maxMembers는 1 이상이어야 합니다.")
        Integer maxMembers,

        @NotNull(message = "category는 필수 입력값입니다.")
        GroupCategory category,

        @NotNull(message = "passwordRequired는 필수 입력값입니다.")
        Boolean passwordRequired,

        String password
    ) {
        public UpdateGroupData.Command toCommand(Actor actor, Long groupId) {
            return new UpdateGroupData.Command(
                actor,
                groupId,
                name,
                description,
                maxMembers,
                category,
                passwordRequired,
                password
            );
        }
    }
}
