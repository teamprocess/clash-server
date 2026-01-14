package com.process.clash.adapter.web.record.dto;

import com.process.clash.application.record.dto.StartRecordData;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public class StartRecordDto {

    public record Request(
            @NotNull(message = "taskId는 필수 입력값입니다.")
            Long taskId
    ) {}

    public record Response(
       Instant startedAt
    ) {

        public static Response from(StartRecordData.Result result) {
            return new Response(
                    result.startedAt()
            );
        }
    }
}
