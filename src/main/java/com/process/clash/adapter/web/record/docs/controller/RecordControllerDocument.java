package com.process.clash.adapter.web.record.docs.controller;

import com.process.clash.adapter.web.record.docs.request.RecordSettingRequestDocument;
import com.process.clash.adapter.web.record.docs.request.StartRecordRequestDocument;
import com.process.clash.adapter.web.record.docs.response.GetTodayRecordResponseDocument;
import com.process.clash.adapter.web.record.docs.response.RecordSettingResponseDocument;
import com.process.clash.adapter.web.record.docs.response.StartRecordResponseDocument;
import com.process.clash.adapter.web.record.docs.response.StopRecordResponseDocument;
import com.process.clash.adapter.web.record.dto.GetTodayRecordDto;
import com.process.clash.adapter.web.record.dto.RecordSettingDto;
import com.process.clash.adapter.web.record.dto.StartRecordDto;
import com.process.clash.adapter.web.record.dto.StopRecordDto;
import com.process.clash.application.common.actor.Actor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "기록 API", description = "일반 기록")
public interface RecordControllerDocument {

    @Operation(summary = "오늘의 기록 조회", description = "오늘의 기록 현황을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetTodayRecordResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "오늘의 일반 기록 현황을 조회했습니다.",
                                      "data": {
                                        "date": "2025-01-02",
                                        "pomodoroEnabled": true,
                                        "totalStudyTime": 7200,
                                        "studyStoppedAt": "2025-01-02T14:30:00Z",
                                        "sessions": [
                                          {
                                            "startedAt": "2025-01-02T09:00:00Z",
                                            "endedAt": "2025-01-02T10:00:00Z",
                                            "task": {
                                              "id": 1,
                                              "name": "자료구조"
                                            }
                                          }
                                        ]
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetTodayRecordDto.Response> getTodayRecord(
            @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "기록 설정 조회", description = "일반 기록 설정을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = RecordSettingResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "일반 기록 설정을 조회했습니다.",
                                      "data": {
                                        "pomodoroEnabled": true,
                                        "studyMinute": 50,
                                        "breakMinute": 10
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<RecordSettingDto.Response> getRecordSetting(
            @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "기록 설정 변경", description = "일반 기록 설정을 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "변경 성공",
                    content = @Content(
                            schema = @Schema(implementation = RecordSettingResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "일반 기록 설정을 변경했습니다.",
                                      "data": {
                                        "pomodoroEnabled": true,
                                        "studyMinute": 45,
                                        "breakMinute": 10
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<RecordSettingDto.Response> updateRecordSetting(
            @RequestBody(description = "기록 설정 변경 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = RecordSettingRequestDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "pomodoroEnabled": true,
                                      "studyMinute": 45,
                                      "breakMinute": 10
                                    }
                                    """)
                    ))
            RecordSettingDto.Request request,
            @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "기록 시작", description = "일반 기록을 시작합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "시작 성공",
                    content = @Content(
                            schema = @Schema(implementation = StartRecordResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "일반 기록을 시작했습니다.",
                                      "data": {
                                        "startedTime": "2025-01-02T09:00:00Z"
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<StartRecordDto.Response> startRecord(
            @RequestBody(description = "기록 시작 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = StartRecordRequestDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "taskId": 1
                                    }
                                    """)
                    ))
            StartRecordDto.Request request,
            @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "기록 종료", description = "진행 중인 기록을 종료합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "종료 성공",
                    content = @Content(
                            schema = @Schema(implementation = StopRecordResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "일반 기록을 중지했습니다.",
                                      "data": {
                                        "taskId": 1,
                                        "stoppedAt": "2025-01-02T10:00:00Z"
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<StopRecordDto.Response> stopRecord(
            @Parameter(hidden = true) Actor actor
    );
}
