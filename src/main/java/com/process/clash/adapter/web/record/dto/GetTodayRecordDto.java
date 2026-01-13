package com.process.clash.adapter.web.record.dto;

import com.process.clash.application.record.dto.GetTodayRecordData;
import com.process.clash.domain.record.model.entity.Task;
import java.util.Date;
import java.util.List;

public class GetTodayRecordDto {

    public record Response(
        String date,
        Boolean pomodoro_enabled,
        Long totalStudyTime,
        Date studyStoppedAt,
        List<Task> tasks
    ) {
        public static Response from(GetTodayRecordData.Result result){
            return new Response(
                result.date(),
                result.pomodoro_enabled(),
                result.totalStudyTime(),
                result.studyStoppedAt(),
                result.tasks()
            );
        }
    }
}
