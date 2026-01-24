package com.process.clash.adapter.web.group.dto;

import com.process.clash.application.group.vo.GroupOwnerVo;
import com.process.clash.application.group.vo.GroupSummaryVo;
import com.process.clash.domain.group.enums.GroupCategory;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "GroupSummary")
public record GroupSummaryDto(
    @Schema(description = "그룹 ID", example = "10")
    Long id,
    @Schema(description = "그룹명", example = "알고리즘 스터디")
    String name,
    @Schema(description = "그룹 설명", example = "주 2회 문제 풀이")
    String description,
    @Schema(description = "최대 인원", example = "10")
    Integer maxMembers,
    @Schema(description = "현재 인원", example = "6")
    Integer currentMemberCount,
    @Schema(description = "카테고리", example = "CLASS")
    GroupCategory category,
    @Schema(description = "비밀번호 필요 여부", example = "false")
    Boolean passwordRequired,
    @Schema(description = "그룹장 정보")
    Owner owner,
    @Schema(description = "내 참여 여부", example = "true")
    Boolean isMember
) {
    public static GroupSummaryDto from(GroupSummaryVo vo) {
        return from(vo, false);
    }

    public static GroupSummaryDto from(GroupSummaryVo vo, Boolean isMember) {
        return new GroupSummaryDto(
            vo.id(),
            vo.name(),
            vo.description(),
            vo.maxMembers(),
            vo.currentMemberCount(),
            vo.category(),
            vo.passwordRequired(),
            Owner.from(vo.owner()),
            isMember
        );
    }

    public record Owner(
        @Schema(description = "유저 ID", example = "1")
        Long id,
        @Schema(description = "이름", example = "홍길동")
        String name
    ) {
        public static Owner from(GroupOwnerVo owner) {
            return new Owner(owner.id(), owner.name());
        }
    }
}
