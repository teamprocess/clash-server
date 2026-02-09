package com.process.clash.adapter.web.group.docs.response;

import com.process.clash.domain.group.enums.GroupCategory;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "GroupSummary")
public class GroupSummaryDocument {

    @Schema(description = "그룹 ID", example = "10")
    public Long id;

    @Schema(description = "그룹명", example = "알고리즘 스터디")
    public String name;

    @Schema(description = "그룹 설명", example = "주 2회 문제 풀이")
    public String description;

    @Schema(description = "최대 인원", example = "10")
    public Integer maxMembers;

    @Schema(description = "현재 인원", example = "6")
    public Integer currentMemberCount;

    @Schema(description = "카테고리", example = "CLASS")
    public GroupCategory category;

    @Schema(description = "비밀번호 필요 여부", example = "false")
    public Boolean passwordRequired;

    @Schema(description = "그룹장 정보")
    public Owner owner;

    @Schema(description = "내 참여 여부", example = "true")
    public Boolean isMember;

    public static class Owner {
        @Schema(description = "유저 ID", example = "1")
        public Long id;

        @Schema(description = "이름", example = "홍길동")
        public String name;
    }
}
