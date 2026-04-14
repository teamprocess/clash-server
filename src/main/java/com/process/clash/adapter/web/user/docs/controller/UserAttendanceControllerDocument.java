package com.process.clash.adapter.web.user.docs.controller;

import com.process.clash.adapter.web.user.dto.GetWeeklyAttendanceDto;
import com.process.clash.adapter.web.user.dto.MarkAttendanceDto;
import com.process.clash.application.common.actor.Actor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "출석 API", description = "출석 체크 및 주간 출석 현황 조회")
public interface UserAttendanceControllerDocument {

    @Operation(summary = "출석 체크", description = "오늘 출석을 기록하고 쿠키를 지급합니다. 일일 300 쿠키, 일~토 주간 개근 완료 시 해당 날(토요일)에는 1000 쿠키를 지급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "출석 성공",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "출석이 완료되었습니다.",
                                      "data": {
                                        "attendanceStreak": 5,
                                        "earnedCookies": 300
                                      }
                                    }
                                    """)
                    )),
            @ApiResponse(responseCode = "404", description = "오늘 출석 레코드 없음",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "message": "출석 정보를 찾을 수 없습니다."
                                    }
                                    """)
                    )),
            @ApiResponse(responseCode = "409", description = "이미 출석 완료",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "message": "이미 출석했습니다."
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<MarkAttendanceDto.Response> markAttendance(
            @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "주간 출석 현황 조회", description = "이번 주(일~토) 날짜별 출석 여부를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "주간 출석 현황을 성공적으로 조회했습니다.",
                                      "data": {
                                        "weekStart": "2026-04-12",
                                        "weekEnd": "2026-04-18",
                                        "days": [
                                          { "date": "2026-04-12", "dayOfWeek": "SUNDAY",    "isAttended": true  },
                                          { "date": "2026-04-13", "dayOfWeek": "MONDAY",    "isAttended": true  },
                                          { "date": "2026-04-14", "dayOfWeek": "TUESDAY",   "isAttended": false },
                                          { "date": "2026-04-15", "dayOfWeek": "WEDNESDAY", "isAttended": false },
                                          { "date": "2026-04-16", "dayOfWeek": "THURSDAY",  "isAttended": false },
                                          { "date": "2026-04-17", "dayOfWeek": "FRIDAY",    "isAttended": false },
                                          { "date": "2026-04-18", "dayOfWeek": "SATURDAY",  "isAttended": false }
                                        ]
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetWeeklyAttendanceDto.Response> getWeeklyAttendance(
            @Parameter(hidden = true) Actor actor
    );
}