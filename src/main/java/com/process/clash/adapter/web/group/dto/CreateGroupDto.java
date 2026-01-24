package com.process.clash.adapter.web.group.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.group.data.CreateGroupData;
import com.process.clash.domain.group.enums.GroupCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateGroupDto {

    public record Request(
        @Schema(description = "그룹명", example = "알고리즘 스터디")
        @NotBlank(message = "name은 필수 입력값입니다.")
        String name,

        @Schema(description = "그룹 설명", example = "주 2회 문제 풀이")
        @NotBlank(message = "description은 필수 입력값입니다.")
        String description,

        @Schema(description = "최대 인원", example = "10")
        @NotNull(message = "maxMembers는 필수 입력값입니다.")
        @Min(value = 1, message = "maxMembers는 1 이상이어야 합니다.")
        Integer maxMembers,

        @Schema(description = "카테고리", example = "CLASS")
        @NotNull(message = "category는 필수 입력값입니다.")
        GroupCategory category,

        @Schema(description = "비밀번호 필요 여부", example = "false")
        @NotNull(message = "passwordRequired는 필수 입력값입니다.")
        Boolean passwordRequired,

        @Schema(description = "비밀번호 (필요 시)", example = "1234")
        String password
    ) {
        public CreateGroupData.Command toCommand(Actor actor) {
            return new CreateGroupData.Command(
                actor,
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
