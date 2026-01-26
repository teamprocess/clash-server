package com.process.clash.adapter.web.group.docs.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "그룹 활동 멤버")
public class GroupActivityMemberDoc {

    @Schema(description = "멤버 ID", example = "12")
    public Long id;

    @Schema(description = "이름", example = "홍길동")
    public String name;

    @Schema(description = "학습 시간(초)", example = "3600")
    public Long studyTime;

    @Schema(description = "현재 학습 중 여부", example = "true")
    public Boolean isStudying;
}
