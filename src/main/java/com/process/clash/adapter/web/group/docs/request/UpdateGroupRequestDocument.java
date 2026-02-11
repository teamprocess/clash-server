package com.process.clash.adapter.web.group.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "그룹 수정 요청")
public class UpdateGroupRequestDocument {

    @Schema(description = "그룹명", example = "알고리즘 스터디")
    public String name;

    @Schema(description = "그룹 설명", example = "주 2회 문제 풀이")
    public String description;

    @Schema(description = "최대 인원", example = "10")
    public Integer maxMembers;

    @Schema(description = "카테고리", example = "CLASS")
    public String category;

    @Schema(description = "비밀번호 필요 여부", example = "false")
    public Boolean passwordRequired;

    @Schema(description = "비밀번호 (필요 시)", example = "1234")
    public String password;
}
