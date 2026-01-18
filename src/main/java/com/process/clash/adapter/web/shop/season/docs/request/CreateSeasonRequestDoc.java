package com.process.clash.adapter.web.shop.season.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "시즌 생성 요청")
public class CreateSeasonRequestDoc {

    @Schema(description = "시즌명")
    public String name;

    @Schema(description = "시작일")
    public LocalDate startDate;

    @Schema(description = "종료일")
    public LocalDate endDate;
}
