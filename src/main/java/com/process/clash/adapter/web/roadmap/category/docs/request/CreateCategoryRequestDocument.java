package com.process.clash.adapter.web.roadmap.category.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카테고리 생성 요청")
public class CreateCategoryRequestDocument {

    @Schema(description = "카테고리 이름")
    public String name;
}
