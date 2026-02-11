package com.process.clash.adapter.web.record.dto;

import com.process.clash.application.record.data.GetTodayRecordData;

import java.time.Instant;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

public class GetTodayRecordDto {

    @Schema(name = "GetTodayRecordDtoResponse")

    public record Response(
        String date,
        Boolean pomodoroEnabled,
        Long totalStudyTime,
        Instant studyStoppedAt,
        List<RecordSessionDto.Session> sessions
    ) {
        public static Response from(GetTodayRecordData.Result result){
            return new Response(
                result.date(),
                result.pomodoroEnabled(),
                result.totalStudyTime(),
                result.studyStoppedAt(),
                result.sessions().stream()
                    .map(RecordSessionDto.Session::from)
                    .toList()
            );
        }
    }
}
