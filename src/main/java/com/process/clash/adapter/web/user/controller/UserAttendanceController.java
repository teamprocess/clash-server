package com.process.clash.adapter.web.user.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.adapter.web.user.docs.controller.UserAttendanceControllerDocument;
import com.process.clash.adapter.web.user.dto.GetWeeklyAttendanceDto;
import com.process.clash.adapter.web.user.dto.MarkAttendanceDto;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.user.userattendance.data.GetWeeklyAttendanceData;
import com.process.clash.application.user.userattendance.data.MarkAttendanceData;
import com.process.clash.application.user.userattendance.port.in.GetWeeklyAttendanceUseCase;
import com.process.clash.application.user.userattendance.port.in.MarkAttendanceUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/me/attendance")
@RequiredArgsConstructor
public class UserAttendanceController implements UserAttendanceControllerDocument {

    private final MarkAttendanceUseCase markAttendanceUseCase;
    private final GetWeeklyAttendanceUseCase getWeeklyAttendanceUseCase;

    @PostMapping
    public ApiResponse<MarkAttendanceDto.Response> markAttendance(
            @AuthenticatedActor Actor actor
    ) {
        MarkAttendanceData.Command command = new MarkAttendanceData.Command(actor);
        MarkAttendanceData.Result result = markAttendanceUseCase.execute(command);
        MarkAttendanceDto.Response response = MarkAttendanceDto.Response.from(result);
        return ApiResponse.success(response, "출석이 완료되었습니다.");
    }

    @GetMapping("/weekly")
    public ApiResponse<GetWeeklyAttendanceDto.Response> getWeeklyAttendance(
            @AuthenticatedActor Actor actor
    ) {
        GetWeeklyAttendanceData.Command command = new GetWeeklyAttendanceData.Command(actor);
        GetWeeklyAttendanceData.Result result = getWeeklyAttendanceUseCase.execute(command);
        GetWeeklyAttendanceDto.Response response = GetWeeklyAttendanceDto.Response.from(result);
        return ApiResponse.success(response, "주간 출석 현황을 성공적으로 조회했습니다.");
    }
}