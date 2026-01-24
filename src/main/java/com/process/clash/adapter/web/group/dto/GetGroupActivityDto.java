package com.process.clash.adapter.web.group.dto;

import com.process.clash.application.common.pagination.Pagination;
import com.process.clash.application.group.data.GetGroupActivityData;
import com.process.clash.application.group.vo.GroupMemberVo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public class GetGroupActivityDto {

    @Schema(name = "GetGroupActivityRequest")
    public record Request(
        @Schema(description = "페이지 번호 (1부터)", example = "1")
        Integer page
    ) {}

    @Schema(name = "GetGroupActivityResponse")
    public record Response(
        @Schema(description = "멤버 목록")
        List<Member> members,
        @Schema(description = "페이지네이션")
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
        @Schema(description = "멤버 ID", example = "12")
        Long id,
        @Schema(description = "이름", example = "홍길동")
        String name,
        @Schema(description = "학습 시간(초)", example = "3600")
        Long studyTime,
        @Schema(description = "현재 학습 중 여부", example = "true")
        Boolean isStudying
    ) {
        public static Member from(GroupMemberVo member) {
            return new Member(member.id(), member.name(), member.studyTime(), member.isStudying());
        }
    }

}
