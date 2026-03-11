package com.process.clash.application.record.v2.data;

import com.process.clash.application.common.actor.Actor;
import java.time.LocalDate;
import java.util.List;

public class GetAllSubjectsV2Data {

    public record Command(
        Actor actor,
        LocalDate date
    ) {
    }

    public record Result(
        List<SubjectSummary> subjects
    ) {
        public static Result from(List<SubjectSummary> subjects) {
            return new Result(subjects);
        }
    }

    public record SubjectSummary(
        Long id,
        String name,
        Long studyTime,
        List<TaskSummary> tasks
    ) {
    }

    public record TaskSummary(
        Long id,
        String name,
        boolean completed,
        Long studyTime
    ) {
    }
}
