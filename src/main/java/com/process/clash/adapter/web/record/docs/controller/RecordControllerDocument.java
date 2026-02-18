package com.process.clash.adapter.web.record.docs.controller;

import com.process.clash.adapter.web.record.docs.request.StartRecordRequestDocument;
import com.process.clash.adapter.web.record.docs.request.SwitchActivityAppRequestDocument;
import com.process.clash.adapter.web.record.docs.response.GetCurrentRecordResponseDocument;
import com.process.clash.adapter.web.record.docs.response.GetMonitoredAppsResponseDocument;
import com.process.clash.adapter.web.record.docs.response.GetTodayRecordResponseDocument;
import com.process.clash.adapter.web.record.docs.response.StartRecordResponseDocument;
import com.process.clash.adapter.web.record.docs.response.StopRecordResponseDocument;
import com.process.clash.adapter.web.record.docs.response.SwitchActivityAppResponseDocument;
import com.process.clash.adapter.web.record.dto.GetMonitoredAppsDto;
import com.process.clash.adapter.web.record.dto.GetTodayRecordDto;
import com.process.clash.adapter.web.record.dto.RecordSessionDto;
import com.process.clash.adapter.web.record.dto.StartRecordDto;
import com.process.clash.adapter.web.record.dto.StopRecordDto;
import com.process.clash.adapter.web.record.dto.SwitchActivityAppDto;
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
                                      "message": "오늘의 기록 현황을 조회했습니다.",
                                      "data": {
                                        "date": "2025-01-02",
                                        "totalStudyTime": 7200,
                                        "studyStoppedAt": "2025-01-02T14:30:00Z",
                                        "sessions": [
                                          {
                                            "id": 100,
                                            "recordType": "TASK",
                                            "startedAt": "2025-01-02T09:00:00Z",
                                            "endedAt": "2025-01-02T10:00:00Z",
                                            "task": {
                                              "id": 1,
                                              "name": "자료구조"
                                            },
                                            "activity": null
                                          },
                                          {
                                            "id": 101,
                                            "recordType": "ACTIVITY",
                                            "startedAt": "2025-01-02T10:30:00Z",
                                            "endedAt": "2025-01-02T11:00:00Z",
                                            "task": null,
                                            "activity": {
                                              "appName": "Code"
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

    @Operation(summary = "현재 기록 세션 조회", description = "현재 진행 중인 기록 세션을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetCurrentRecordResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "현재 기록 세션을 조회했습니다.",
                                      "data": {
                                        "id": 100,
                                        "recordType": "ACTIVITY",
                                        "startedAt": "2025-01-02T09:00:00Z",
                                        "endedAt": null,
                                        "task": null,
                                        "activity": {
                                          "appName": "Code"
                                        }
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<RecordSessionDto.Session> getCurrentRecord(
            @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "활동 기록 앱 목록 조회", description = "활동 기록 가능한 앱 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetMonitoredAppsResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "활동 기록 가능 앱 목록을 조회했습니다.",
                                      "data": {
                                        "apps": [
                                          "Code",
                                          "Visual Studio Code",
                                          "WebStorm"
                                        ]
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetMonitoredAppsDto.Response> getMonitoredApps(
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
                                      "message": "기록을 시작했습니다.",
                                      "data": {
                                        "startedTime": "2025-01-02T09:00:00Z",
                                        "session": {
                                          "id": 100,
                                          "recordType": "TASK",
                                          "startedAt": "2025-01-02T09:00:00Z",
                                          "endedAt": null,
                                          "task": {
                                            "id": 1,
                                            "name": "자료구조"
                                          },
                                          "activity": null
                                        }
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
                                      "recordType": "TASK",
                                      "taskId": 1,
                                      "appName": null
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
                                      "message": "기록을 중지했습니다.",
                                      "data": {
                                        "stoppedAt": "2025-01-02T10:00:00Z",
                                        "session": {
                                          "id": 100,
                                          "recordType": "TASK",
                                          "startedAt": "2025-01-02T09:00:00Z",
                                          "endedAt": "2025-01-02T10:00:00Z",
                                          "task": {
                                            "id": 1,
                                            "name": "자료구조"
                                          },
                                          "activity": null
                                        }
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<StopRecordDto.Response> stopRecord(
            @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "활동 앱 전환", description = "진행 중인 활동 기록 세션의 앱을 전환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "전환 성공",
                    content = @Content(
                            schema = @Schema(implementation = SwitchActivityAppResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "활동 앱을 전환했습니다.",
                                      "data": {
                                        "switchedAt": "2026-02-12T10:10:00Z",
                                        "session": {
                                          "id": 100,
                                          "recordType": "ACTIVITY",
                                          "startedAt": "2026-02-12T09:00:00Z",
                                          "endedAt": null,
                                          "task": null,
                                          "activity": {
                                            "appName": "IntelliJ IDEA"
                                          }
                                        }
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<SwitchActivityAppDto.Response> switchActivityApp(
            @RequestBody(description = "활동 앱 전환 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = SwitchActivityAppRequestDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "appName": "IntelliJ IDEA"
                                    }
                                    """)
                    ))
            SwitchActivityAppDto.Request request,
            @Parameter(hidden = true) Actor actor
    );
}
