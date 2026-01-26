package com.process.clash.adapter.web.group.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.group.data.JoinGroupData;
public class JoinGroupDto {

    public record Request(
        String password
    ) {
        public JoinGroupData.Command toCommand(Actor actor, Long groupId) {
            return new JoinGroupData.Command(actor, groupId, password);
        }
    }
}
