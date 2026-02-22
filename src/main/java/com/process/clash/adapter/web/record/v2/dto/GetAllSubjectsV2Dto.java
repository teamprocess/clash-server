package com.process.clash.adapter.web.record.v2.dto;

import com.process.clash.application.record.v2.data.GetAllSubjectsV2Data;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public class GetAllSubjectsV2Dto {

    private static final String DEFAULT_ICON = "timer";

    @Schema(name = "GetAllSubjectsV2DtoResponse")
    public record Response(
        List<SubjectSummary> subjects
    ) {
        public static Response from(GetAllSubjectsV2Data.Result result) {
            return new Response(
                result.subjects().stream()
                    .map(SubjectSummary::from)
                    .toList()
            );
        }
    }

    public record SubjectSummary(
        Long id,
        String name,
        String icon,
        Long studyTime,
        List<TaskSummary> tasks
    ) {
        public static SubjectSummary from(GetAllSubjectsV2Data.SubjectSummary subject) {
            return new SubjectSummary(
                subject.id(),
                subject.name(),
                DEFAULT_ICON,
                subject.studyTime(),
                subject.tasks().stream().map(TaskSummary::from).toList()
            );
        }
    }

    public record TaskSummary(
        Long id,
        String name,
        String icon,
        Long studyTime
    ) {
        public static TaskSummary from(GetAllSubjectsV2Data.TaskSummary task) {
            return new TaskSummary(
                task.id(),
                task.name(),
                DEFAULT_ICON,
                task.studyTime()
            );
        }
    }
}
