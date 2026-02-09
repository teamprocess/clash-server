package com.process.clash.adapter.web.user.docs.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "캘린더 일자 정보")
public class GetMyActivityCalendarDayDocument {

    @Schema(description = "날짜", example = "2025-01-01")
    public String date;

    @Schema(description = "학습 시간(초)", example = "3600")
    public Integer studyTime;
}
