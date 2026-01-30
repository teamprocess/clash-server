package com.process.clash.adapter.web.user.docs.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "활동 캘린더 데이터")
public class GetMyActivityCalendarDataDoc {

    @ArraySchema(schema = @Schema(implementation = GetMyActivityCalendarDayDoc.class))
    public List<GetMyActivityCalendarDayDoc> calendar;
}
