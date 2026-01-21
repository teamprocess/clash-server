package com.process.clash.adapter.web.compete.rival.rival.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "라이벌 승인/거절 요청")
public class ModifyRivalRequestDoc {

    @Schema(description = "라이벌 ID", example = "3")
    public Long id;
}