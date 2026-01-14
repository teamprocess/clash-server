package com.process.clash.adapter.web.record.dto;

import com.process.clash.application.record.dto.GetAllTasksData;
import com.process.clash.domain.record.model.entity.Task;
import java.util.List;

public class GetAllTasksDto {

    public record Response(
        List<Task> tasks
    ) {
        public static Response from(GetAllTasksData.Result result){
            return new Response(
                result.tasks()
            );
        }
    }
}
