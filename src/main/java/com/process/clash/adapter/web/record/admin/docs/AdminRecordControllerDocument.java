package com.process.clash.adapter.web.record.admin.docs;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.record.admin.dto.CutUserRecordAdminDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "어드민 기록 API", description = "어드민 기록 관리")
public interface AdminRecordControllerDocument {

    @Operation(
        summary = "사용자 기록 강제 중단",
        description = "특정 사용자의 현재 진행 중인 기록을 강제로 중단합니다. ended_at을 저장하지만 EXP는 지급하지 않습니다."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "중단 성공",
            content = @Content(
                schema = @Schema(implementation = CutUserRecordAdminDto.Response.class),
                examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "message": "사용자의 기록을 강제 중단했습니다.",
                      "data": {
                        "endedAt": "2026-03-10T12:00:00Z",
                        "session": {
                          "id": 100,
                          "sessionType": "TASK",
                          "startedAt": "2026-03-10T09:00:00Z",
                          "endedAt": "2026-03-10T12:00:00Z",
                          "subject": {
                            "id": 1,
                            "name": "백엔드 프로젝트"
                          },
                          "task": {
                            "id": 10,
                            "name": "ERD 설계"
                          },
                          "develop": null
                        }
                      }
                    }
                    """)
            )),
        @ApiResponse(responseCode = "404", description = "진행 중인 기록 세션 없음",
            content = @Content(
                examples = @ExampleObject(value = """
                    {
                      "success": false,
                      "message": "진행 중인 기록 세션을 찾을 수 없습니다."
                    }
                    """)
            ))
    })
    ApiResponse<CutUserRecordAdminDto.Response> cutUserRecord(
        @Parameter(description = "기록을 중단할 사용자 ID", example = "42", required = true)
        Long userId
    );
}