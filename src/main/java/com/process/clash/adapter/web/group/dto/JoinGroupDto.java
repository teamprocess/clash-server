package com.process.clash.adapter.web.group.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.group.data.JoinGroupData;
import io.swagger.v3.oas.annotations.media.Schema;

public class JoinGroupDto {

    @Schema(name = "JoinGroupRequest")
    public record Request(
        @Schema(description = "비밀번호 (필요 시)", example = "1234")
        String password
    ) {
        public JoinGroupData.Command toCommand(Actor actor, Long groupId) {
            return new JoinGroupData.Command(actor, groupId, password);
        }
    }
}
