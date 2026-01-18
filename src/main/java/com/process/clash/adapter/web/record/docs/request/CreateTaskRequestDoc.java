package com.process.clash.adapter.web.record.docs.request;

import com.process.clash.domain.record.model.enums.TaskColor;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "과목 생성 요청")
public class CreateTaskRequestDoc {

    @Schema(description = "과목명")
    public String name;

    @Schema(description = "색상")
    public TaskColor color;
}
