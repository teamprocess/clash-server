package com.process.clash.adapter.web.group.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "그룹 참여 요청")
public class JoinGroupRequestDocument {

    @Schema(description = "비밀번호 (필요 시)", example = "1234")
    public String password;
}
