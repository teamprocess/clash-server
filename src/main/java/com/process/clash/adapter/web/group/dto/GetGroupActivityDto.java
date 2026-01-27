package com.process.clash.adapter.web.group.dto;

import com.process.clash.application.common.pagination.Pagination;
import com.process.clash.application.group.data.GetGroupActivityData;
import com.process.clash.application.group.vo.GroupMemberVo;
import java.util.List;

public class GetGroupActivityDto {

    public record Request(
        Integer page
    ) {}

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
