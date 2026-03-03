package com.process.clash.adapter.web.record.v2.docs.controller;

import com.process.clash.adapter.web.record.v2.docs.request.StartRecordV2RequestDocument;
import com.process.clash.adapter.web.record.v2.docs.request.SwitchDevelopAppV2RequestDocument;
import com.process.clash.adapter.web.record.v2.docs.response.GetActivityStatisticsV2ResponseDocument;
import com.process.clash.adapter.web.record.v2.docs.response.GetCurrentRecordV2ResponseDocument;
import com.process.clash.adapter.web.record.v2.docs.response.GetDailyRecordV2ResponseDocument;
import com.process.clash.adapter.web.record.v2.docs.response.GetMonitoredAppsV2ResponseDocument;
import com.process.clash.adapter.web.record.v2.docs.response.StartRecordV2ResponseDocument;
import com.process.clash.adapter.web.record.v2.docs.response.StopRecordV2ResponseDocument;
import com.process.clash.adapter.web.record.v2.docs.response.SwitchDevelopAppV2ResponseDocument;
import com.process.clash.adapter.web.record.v2.dto.GetActivityStatisticsV2Dto;
import com.process.clash.adapter.web.record.v2.dto.GetMonitoredAppsV2Dto;
import com.process.clash.adapter.web.record.v2.dto.GetTodayRecordV2Dto;
import com.process.clash.adapter.web.record.v2.dto.RecordSessionV2Dto;
import com.process.clash.adapter.web.record.v2.dto.StartRecordV2Dto;
import com.process.clash.adapter.web.record.v2.dto.StopRecordV2Dto;
import com.process.clash.adapter.web.record.v2.dto.SwitchDevelopAppV2Dto;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.common.enums.PeriodCategory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "기록 API V2", description = "V2 기록")
public interface RecordV2ControllerDocument {

    @Operation(summary = "일자별 기록 조회", description = "특정 날짜의 V2 기록 현황을 조회합니다. 날짜 미입력 시 오늘 기록을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(
                schema = @Schema(implementation = GetDailyRecordV2ResponseDocument.class),
                examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "message": "기록 현황을 조회했습니다.",
                      "data": {
                        "date": "2026-02-22",
                        "totalStudyTime": 7200,
                        "studyStoppedAt": "2026-02-22T14:30:00Z",
                        "sessions": [
                          {
                            "id": 100,
                            "sessionType": "TASK",
                            "startedAt": "2026-02-22T09:00:00Z",
                            "endedAt": "2026-02-22T10:00:00Z",
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
                        ]
                      }
                    }
                    """)
            ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetTodayRecordV2Dto.Response> getDailyRecord(
        @Parameter(hidden = true) Actor actor,
        @Parameter(description = "조회 날짜 (yyyy-MM-dd), 미입력 시 오늘", example = "2026-02-22")
        @RequestParam(required = false) LocalDate date
    );

    @Operation(summary = "현재 기록 세션 조회", description = "현재 진행 중인 V2 기록 세션을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(
                schema = @Schema(implementation = GetCurrentRecordV2ResponseDocument.class),
                examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "message": "현재 기록 세션을 조회했습니다.",
                      "data": {
                        "id": 101,
                        "sessionType": "DEVELOP",
                        "startedAt": "2026-02-22T10:30:00Z",
                        "endedAt": null,
                        "subject": null,
                        "task": null,
                        "develop": {
                          "appId": "VSCODE"
                        }
                      }
                    }
                    """)
            ))
    })
    com.process.clash.adapter.web.common.ApiResponse<RecordSessionV2Dto.Session> getCurrentRecord(
        @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "개발 앱 목록 조회", description = "V2 개발 기록 가능한 앱 목록을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(
                schema = @Schema(implementation = GetMonitoredAppsV2ResponseDocument.class),
                examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "message": "활동 기록 가능 앱 목록을 조회했습니다.",
                      "data": {
                        "apps": [
                          "VSCODE",
                          "INTELLIJ_IDEA",
                          "WEBSTORM"
                        ]
                      }
                    }
                    """)
            ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetMonitoredAppsV2Dto.Response> getMonitoredApps(
        @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "앱별 활동 시간 조회", description = "지정 기간(DAY/WEEK/MONTH) 동안 앱별 하루 평균 활동 시간을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(
                schema = @Schema(implementation = GetActivityStatisticsV2ResponseDocument.class),
                examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "message": "앱별 활동 시간을 조회했습니다.",
                      "data": {
                        "apps": [
                          {
                            "appId": "VSCODE",
                            "studyTime": 3600
                          },
                          {
                            "appId": "INTELLIJ_IDEA",
                            "studyTime": 1200
                          }
                        ]
                      }
                    }
                    """)
            ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetActivityStatisticsV2Dto.Response> getActivityStatistics(
        @Parameter(hidden = true) Actor actor,
        @Parameter(description = "조회 기간 (DAY, WEEK, MONTH)", example = "WEEK", required = true)
        @RequestParam PeriodCategory duration
    );

    @Operation(summary = "기록 시작", description = "V2 기록을 시작합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "시작 성공",
            content = @Content(
                schema = @Schema(implementation = StartRecordV2ResponseDocument.class),
                examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "message": "기록을 시작했습니다.",
                      "data": {
                        "startedAt": "2026-02-22T09:00:00Z",
                        "session": {
                          "id": 100,
                          "sessionType": "TASK",
                          "startedAt": "2026-02-22T09:00:00Z",
                          "endedAt": null,
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
            ))
    })
    com.process.clash.adapter.web.common.ApiResponse<StartRecordV2Dto.Response> startRecord(
        @RequestBody(description = "V2 기록 시작 요청", required = true,
            content = @Content(
                schema = @Schema(implementation = StartRecordV2RequestDocument.class),
                examples = @ExampleObject(value = """
                    {
                      "sessionType": "TASK",
                      "subjectId": 1,
                      "taskId": 10,
                      "appId": null
                    }
                    """)
            ))
        StartRecordV2Dto.Request request,
        @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "기록 종료", description = "진행 중인 V2 기록을 종료합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "종료 성공",
            content = @Content(
                schema = @Schema(implementation = StopRecordV2ResponseDocument.class),
                examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "message": "기록을 중지했습니다.",
                      "data": {
                        "stoppedAt": "2026-02-22T11:00:00Z",
                        "session": {
                          "id": 100,
                          "sessionType": "TASK",
                          "startedAt": "2026-02-22T09:00:00Z",
                          "endedAt": "2026-02-22T11:00:00Z",
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
            ))
    })
    com.process.clash.adapter.web.common.ApiResponse<StopRecordV2Dto.Response> stopRecord(
        @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "개발 앱 전환", description = "진행 중인 개발 세션의 앱을 전환합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "전환 성공",
            content = @Content(
                schema = @Schema(implementation = SwitchDevelopAppV2ResponseDocument.class),
                examples = @ExampleObject(value = """
                    {
                      "success": true,
                      "message": "개발 앱을 전환했습니다.",
                      "data": {
                        "switchedAt": "2026-02-22T12:10:00Z",
                        "session": {
                          "id": 101,
                          "sessionType": "DEVELOP",
                          "startedAt": "2026-02-22T10:30:00Z",
                          "endedAt": null,
                          "subject": null,
                          "task": null,
                          "develop": {
                            "appId": "INTELLIJ_IDEA"
                          }
                        }
                      }
                    }
                    """)
            ))
    })
    com.process.clash.adapter.web.common.ApiResponse<SwitchDevelopAppV2Dto.Response> switchDevelopApp(
        @RequestBody(description = "V2 개발 앱 전환 요청", required = true,
            content = @Content(
                schema = @Schema(implementation = SwitchDevelopAppV2RequestDocument.class),
                examples = @ExampleObject(value = """
                    {
                      "appId": "INTELLIJ_IDEA"
                    }
                    """)
            ))
        SwitchDevelopAppV2Dto.Request request,
        @Parameter(hidden = true) Actor actor
    );
}
