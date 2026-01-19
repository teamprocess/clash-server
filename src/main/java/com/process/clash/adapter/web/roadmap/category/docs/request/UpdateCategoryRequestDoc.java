package com.process.clash.adapter.web.roadmap.category.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카테고리 수정 요청")
public class UpdateCategoryRequestDoc {

    @Schema(description = "카테고리 이름")
    public String name;
}
