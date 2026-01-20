package com.process.clash.adapter.web.record.dto;

import com.process.clash.application.record.data.GetRecordSettingData;
import com.process.clash.application.record.data.UpdateRecordSettingData;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

public class RecordSettingDto {

    @Schema(name = "RecordSettingDtoRequest")

    public record Request(
        @NotNull(message = "pomodoroEnabled는 필수 입력값입니다.")
        Boolean pomodoroEnabled,
        @NotNull(message = "studyMinute는 필수 입력값입니다.")
        Integer studyMinute,
        @NotNull(message = "breakMinute는 필수 입력값입니다.")
        Integer breakMinute
    ) {}

    @Schema(name = "RecordSettingDtoResponse")

    public record Response(
        Boolean pomodoroEnabled,
        Integer studyMinute,
        Integer breakMinute
    ) {
        public static Response from(GetRecordSettingData.Result result) {
            return new Response(
                result.pomodoroEnabled(),
                result.studyMinute(),
                result.breakMinute()
            );
        }

        public static Response from(UpdateRecordSettingData.Result result) {
            return new Response(
                result.pomodoroEnabled(),
                result.studyMinute(),
                result.breakMinute()
            );
        }
    }
}
