package com.process.clash.adapter.web.group.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.group.data.GetGroupActivityData;
import com.process.clash.application.group.vo.GroupMemberVo;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public class GetGroupActivityDto {

    public record Request(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        public GetGroupActivityData.Command toCommand(Actor actor, Long groupId) {
            return GetGroupActivityData.Command.of(actor, groupId, date);
        }
    }

    public record Response(List<Member> members) {
        public static Response from(GetGroupActivityData.Result result) {
            List<Member> members = result.members().stream()
                .map(Member::from)
                .toList();

            return new Response(members);
        }
    }

    public record Member(
        Long id,
        String name,
        Long studyTime,
        Boolean isStudying
    ) {
        public static Member from(GroupMemberVo member) {
            return new Member(member.id(), member.name(), member.studyTime(), member.isStudying());
        }
    }

}
