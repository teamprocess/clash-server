package com.process.clash.adapter.web.record.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "과목 수정 요청")
public class UpdateTaskRequestDoc {

    @Schema(description = "과목명")
    public String name;
}
