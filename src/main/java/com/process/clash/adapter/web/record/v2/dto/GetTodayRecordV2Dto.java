package com.process.clash.adapter.web.record.v2.dto;

import com.process.clash.application.record.v2.data.GetTodayRecordV2Data;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;

public class GetTodayRecordV2Dto {

    @Schema(name = "GetTodayRecordV2DtoResponse")
    public record Response(
        String date,
        Long totalStudyTime,
        Instant studyStoppedAt,
        List<RecordSessionV2Dto.Session> sessions
    ) {
        public static Response from(GetTodayRecordV2Data.Result result) {
            return new Response(
                result.date(),
                result.totalStudyTime(),
                result.studyStoppedAt(),
                result.sessions().stream().map(RecordSessionV2Dto.Session::from).toList()
            );
        }
    }
}
