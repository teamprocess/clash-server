package com.process.clash.adapter.web.record.v2.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.record.v2.docs.controller.RecordV2ControllerDocument;
import com.process.clash.adapter.web.record.v2.dto.GetActivityStatisticsV2Dto;
import com.process.clash.adapter.web.record.v2.dto.GetCurrentRecordV2Dto;
import com.process.clash.adapter.web.record.v2.dto.GetMonitoredAppsV2Dto;
import com.process.clash.adapter.web.record.v2.dto.GetTodayRecordV2Dto;
import com.process.clash.adapter.web.record.v2.dto.RecordSessionV2Dto;
import com.process.clash.adapter.web.record.v2.dto.StartRecordV2Dto;
import com.process.clash.adapter.web.record.v2.dto.StopRecordV2Dto;
import com.process.clash.adapter.web.record.v2.dto.SwitchDevelopAppV2Dto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.v2.data.GetCurrentRecordV2Data;
import com.process.clash.application.record.v2.data.GetActivityStatisticsV2Data;
import com.process.clash.application.record.v2.data.GetMonitoredAppsV2Data;
import com.process.clash.application.record.v2.data.GetTodayRecordV2Data;
import com.process.clash.application.record.v2.data.StartRecordV2Data;
import com.process.clash.application.record.v2.data.StopRecordV2Data;
import com.process.clash.application.record.v2.data.SwitchDevelopAppV2Data;
import com.process.clash.application.record.v2.port.in.GetActivityStatisticsV2UseCase;
import com.process.clash.application.record.v2.port.in.GetCurrentRecordV2UseCase;
import com.process.clash.application.record.v2.port.in.GetMonitoredAppsV2UseCase;
import com.process.clash.application.record.v2.port.in.GetTodayRecordV2UseCase;
import com.process.clash.application.record.v2.port.in.StartRecordV2UseCase;
import com.process.clash.application.record.v2.port.in.StopRecordV2UseCase;
import com.process.clash.application.record.v2.port.in.SwitchDevelopAppV2UseCase;
import com.process.clash.domain.common.enums.PeriodCategory;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/record")
@RequiredArgsConstructor
public class RecordV2Controller implements RecordV2ControllerDocument {

    private final GetTodayRecordV2UseCase getTodayRecordV2UseCase;
    private final StartRecordV2UseCase startRecordV2UseCase;
    private final StopRecordV2UseCase stopRecordV2UseCase;
    private final GetCurrentRecordV2UseCase getCurrentRecordV2UseCase;
    private final GetMonitoredAppsV2UseCase getMonitoredAppsV2UseCase;
    private final SwitchDevelopAppV2UseCase switchDevelopAppV2UseCase;
    private final GetActivityStatisticsV2UseCase getActivityStatisticsV2UseCase;

    @GetMapping("/daily")
    public ApiResponse<GetTodayRecordV2Dto.Response> getDailyRecord(
        @AuthenticatedActor Actor actor,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        GetTodayRecordV2Data.Command command = GetTodayRecordV2Data.Command.builder()
            .actor(actor)
            .date(date)
            .build();
        GetTodayRecordV2Data.Result result = getTodayRecordV2UseCase.execute(command);

        return ApiResponse.success(
            GetTodayRecordV2Dto.Response.from(result),
            "기록 현황을 조회했습니다."
        );
    }

    @PostMapping("/start")
    public ApiResponse<StartRecordV2Dto.Response> startRecord(
        @Valid @RequestBody StartRecordV2Dto.Request request,
        @AuthenticatedActor Actor actor
    ) {
        StartRecordV2Data.Command command = request.toCommand(actor);
        StartRecordV2Data.Result result = startRecordV2UseCase.execute(command);

        return ApiResponse.success(
            StartRecordV2Dto.Response.from(result),
            "기록을 시작했습니다."
        );
    }

    @PostMapping("/stop")
    public ApiResponse<StopRecordV2Dto.Response> stopRecord(
        @AuthenticatedActor Actor actor
    ) {
        StopRecordV2Data.Command command = new StopRecordV2Data.Command(actor);
        StopRecordV2Data.Result result = stopRecordV2UseCase.execute(command);

        return ApiResponse.success(
            StopRecordV2Dto.Response.from(result),
            "기록을 중지했습니다."
        );
    }

    @GetMapping("/current")
    public ApiResponse<RecordSessionV2Dto.Session> getCurrentRecord(
        @AuthenticatedActor Actor actor
    ) {
        GetCurrentRecordV2Data.Command command = new GetCurrentRecordV2Data.Command(actor);
        GetCurrentRecordV2Data.Result result = getCurrentRecordV2UseCase.execute(command);
        RecordSessionV2Dto.Session response = GetCurrentRecordV2Dto.from(result);

        if (response == null) {
            return ApiResponse.success(null, "현재 기록 세션이 없습니다.");
        }
        return ApiResponse.success(response, "현재 기록 세션을 조회했습니다.");
    }

    @GetMapping("/activities/monitored-apps")
    public ApiResponse<GetMonitoredAppsV2Dto.Response> getMonitoredApps(
        @AuthenticatedActor Actor actor
    ) {
        GetMonitoredAppsV2Data.Command command = new GetMonitoredAppsV2Data.Command(actor);
        GetMonitoredAppsV2Data.Result result = getMonitoredAppsV2UseCase.execute(command);

        return ApiResponse.success(
            GetMonitoredAppsV2Dto.Response.from(result),
            "활동 기록 가능 앱 목록을 조회했습니다."
        );
    }

    @GetMapping("/activity_statistics")
    public ApiResponse<GetActivityStatisticsV2Dto.Response> getActivityStatistics(
        @AuthenticatedActor Actor actor,
        @RequestParam PeriodCategory duration
    ) {
        GetActivityStatisticsV2Data.Command command = new GetActivityStatisticsV2Data.Command(actor, duration);
        GetActivityStatisticsV2Data.Result result = getActivityStatisticsV2UseCase.execute(command);

        return ApiResponse.success(
            GetActivityStatisticsV2Dto.Response.from(result),
            "앱별 활동 시간을 조회했습니다."
        );
    }

    @PatchMapping("/activities/switch-app")
    public ApiResponse<SwitchDevelopAppV2Dto.Response> switchDevelopApp(
        @Valid @RequestBody SwitchDevelopAppV2Dto.Request request,
        @AuthenticatedActor Actor actor
    ) {
        SwitchDevelopAppV2Data.Command command = request.toCommand(actor);
        SwitchDevelopAppV2Data.Result result = switchDevelopAppV2UseCase.execute(command);

        return ApiResponse.success(
            SwitchDevelopAppV2Dto.Response.from(result),
            "개발 앱을 전환했습니다."
        );
    }
}
