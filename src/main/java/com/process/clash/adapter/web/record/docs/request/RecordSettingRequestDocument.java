package com.process.clash.adapter.web.record.docs.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "일반 기록 설정 변경 요청")
public class RecordSettingRequestDocument {

    @Schema(description = "포모도로 사용 여부")
    public Boolean pomodoroEnabled;

    @Schema(description = "집중 시간(분)")
    public Integer studyMinute;

    @Schema(description = "휴식 시간(분)")
    public Integer breakMinute;
}
