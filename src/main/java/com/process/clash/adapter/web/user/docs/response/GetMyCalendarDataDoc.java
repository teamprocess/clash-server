package com.process.clash.adapter.web.user.docs.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "활동 캘린더 데이터")
public class GetMyCalendarDataDoc {

    @ArraySchema(schema = @Schema(implementation = GetMyCalendarDayDoc.class))
    public List<GetMyCalendarDayDoc> calendar;
}
