package com.process.clash.adapter.web.record.admin.dto;

import com.process.clash.adapter.web.record.v2.dto.RecordSessionV2Dto;
import com.process.clash.application.record.admin.data.CutUserRecordAdminData;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

public class CutUserRecordAdminDto {

    @Schema(name = "CutUserRecordAdminDtoResponse")
    public record Response(
        Instant endedAt,
        RecordSessionV2Dto.Session session
    ) {
        public static Response from(CutUserRecordAdminData.Result result) {
            return new Response(
                result.endedAt(),
                RecordSessionV2Dto.Session.from(result.session())
            );
        }
    }
}