package com.process.clash.adapter.web.record.dto;

import com.process.clash.application.record.dto.GetRecordSettingData;
import com.process.clash.application.record.dto.UpdateRecordSettingData;
import jakarta.validation.constraints.NotNull;

public class RecordSettingDto {

    public record Request(
        @NotNull(message = "pomodoroEnabled는 필수 입력값입니다.")
        Boolean pomodoroEnabled,
        @NotNull(message = "studyMinute는 필수 입력값입니다.")
        Integer studyMinute,
        @NotNull(message = "breakMinute는 필수 입력값입니다.")
        Integer breakMinute
    ) {}

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
