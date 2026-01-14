package com.process.clash.adapter.web.record.dto;

import com.process.clash.application.record.dto.GetTodayRecordData;
import com.process.clash.domain.record.model.entity.Task;
import java.time.Instant;

public class GetTodayRecordDto {

    public record Response(
        String date,
        Boolean pomodoro_enabled,
        Long totalStudyTime,
        Instant studyStoppedAt
    ) {
        public static Response from(GetTodayRecordData.Result result){
            return new Response(
                result.date(),
                result.pomodoro_enabled(),
                result.totalStudyTime(),
                result.studyStoppedAt()
            );
        }
    }
}
