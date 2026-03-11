package com.process.clash.adapter.web.group.dto;

import com.process.clash.application.common.pagination.Pagination;
import com.process.clash.application.group.data.GetGroupActivityData;
import com.process.clash.application.group.vo.GroupMemberVo;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public class GetGroupActivityDto {

    public record Request(
        Integer page,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        public GetGroupActivityData.Command toCommand(com.process.clash.application.common.actor.Actor actor, Long groupId) {
            return GetGroupActivityData.Command.of(actor, groupId, page, date);
        }
    }

    public record Response(
        List<Member> members,
        Pagination pagination
    ) {
        public static Response from(GetGroupActivityData.Result result) {
            List<Member> members = result.members().stream()
                .map(Member::from)
                .toList();

            return new Response(
                members,
                result.pagination()
            );
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
