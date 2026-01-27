package com.process.clash.adapter.web.user.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "활동 캘린더 조회 응답",
        example = """
            {
              "success": true,
              "message": "활동 캘린더 정보를 성공적으로 조회했습니다.",
              "data": {
                "calendar": [
                  {
                    "date": "2025-01-01",
                    "studyTime": 3600
                  }
                ]
              }
            }
            """
)
public class GetMyCalendarResponseDoc extends SuccessResponseDoc {

    @Schema(description = "활동 캘린더")
    public GetMyCalendarDataDoc data;
}
