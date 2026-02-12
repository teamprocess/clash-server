package com.process.clash.adapter.web.record.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.record.dto.GetCurrentRecordDto;
import com.process.clash.adapter.web.record.dto.GetMonitoredAppsDto;
import com.process.clash.adapter.web.record.docs.controller.RecordControllerDocument;
import com.process.clash.adapter.web.record.dto.GetTodayRecordDto;
import com.process.clash.adapter.web.record.dto.RecordSessionDto;
import com.process.clash.adapter.web.record.dto.RecordSettingDto;
import com.process.clash.adapter.web.record.dto.StartRecordDto;
import com.process.clash.adapter.web.record.dto.StopRecordDto;
import com.process.clash.adapter.web.record.dto.SwitchActivityAppDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.data.GetCurrentRecordData;
import com.process.clash.application.record.data.GetMonitoredAppsData;
import com.process.clash.application.record.data.GetRecordSettingData;
import com.process.clash.application.record.data.GetTodayRecordData;
import com.process.clash.application.record.data.StartRecordData;
import com.process.clash.application.record.data.StopRecordData;
import com.process.clash.application.record.data.SwitchActivityAppData;
import com.process.clash.application.record.data.UpdateRecordSettingData;
import com.process.clash.application.record.port.in.GetCurrentRecordUseCase;
import com.process.clash.application.record.port.in.GetMonitoredAppsUseCase;
import com.process.clash.application.record.port.in.GetRecordSettingUseCase;
import com.process.clash.application.record.port.in.GetTodayRecordUseCase;
import com.process.clash.application.record.port.in.StartRecordUseCase;
import com.process.clash.application.record.port.in.StopRecordUseCase;
import com.process.clash.application.record.port.in.SwitchActivityAppUseCase;
import com.process.clash.application.record.port.in.UpdateRecordSettingUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
public class RecordController implements RecordControllerDocument {

    private final GetTodayRecordUseCase getTodayRecordUseCase;
    private final StartRecordUseCase startRecordUseCase;
    private final StopRecordUseCase stopRecordUseCase;
    private final GetRecordSettingUseCase getRecordSettingUseCase;
    private final UpdateRecordSettingUseCase updateRecordSettingUseCase;
    private final GetCurrentRecordUseCase getCurrentRecordUseCase;
    private final GetMonitoredAppsUseCase getMonitoredAppsUseCase;
    private final SwitchActivityAppUseCase switchActivityAppUseCase;

    @GetMapping("/today")
    public ApiResponse<GetTodayRecordDto.Response> getTodayRecord(
            @AuthenticatedActor Actor actor
    ) {

        GetTodayRecordData.Command command = GetTodayRecordData.Command.builder()
            .actor(actor)
            .build();
        GetTodayRecordData.Result result = getTodayRecordUseCase.execute(command);

        return ApiResponse.success(
            GetTodayRecordDto.Response.from(result),
            "오늘의 기록 현황을 조회했습니다."
        );
    }

    @GetMapping("/setting")
    public ApiResponse<RecordSettingDto.Response> getRecordSetting(
        @AuthenticatedActor Actor actor
    ) {
        GetRecordSettingData.Command command = GetRecordSettingData.Command.builder()
            .actor(actor)
            .build();
        GetRecordSettingData.Result result = getRecordSettingUseCase.execute(command);

        return ApiResponse.success(
            RecordSettingDto.Response.from(result),
            "일반 기록 설정을 조회했습니다."
        );
    }

    @PatchMapping("/setting")
    public ApiResponse<RecordSettingDto.Response> updateRecordSetting(
        @Valid @RequestBody RecordSettingDto.Request request,
        @AuthenticatedActor Actor actor
    ) {
        UpdateRecordSettingData.Command command = new UpdateRecordSettingData.Command(
            actor,
            request.pomodoroEnabled(),
            request.studyMinute(),
            request.breakMinute()
        );
        UpdateRecordSettingData.Result result = updateRecordSettingUseCase.execute(command);

        return ApiResponse.success(
            RecordSettingDto.Response.from(result),
            "일반 기록 설정을 변경했습니다."
        );
    }

    @PostMapping("/start")
    public ApiResponse<StartRecordDto.Response> startRecord(
            @Valid @RequestBody StartRecordDto.Request request,
            @AuthenticatedActor Actor actor
    ) {
        StartRecordData.Command command = new StartRecordData.Command(
                request.recordType(),
                request.taskId(),
                request.appName(),
                actor
        );

        StartRecordData.Result result = startRecordUseCase.execute(command);

        return ApiResponse.success(
                StartRecordDto.Response.from(
                        result
                ),
                "기록을 시작했습니다."
        );
    }

    @PostMapping("/stop")
    public ApiResponse<StopRecordDto.Response> stopRecord(
            @AuthenticatedActor Actor actor
    ) {
        StopRecordData.Command command = new StopRecordData.Command(
                actor
        );

        StopRecordData.Result result = stopRecordUseCase.execute(command);

        return ApiResponse.success(
            StopRecordDto.Response.from(result),
            "기록을 중지했습니다."
        );
    }

    @GetMapping("/current")
    public ApiResponse<RecordSessionDto.Session> getCurrentRecord(
        @AuthenticatedActor Actor actor
    ) {
        GetCurrentRecordData.Command command = new GetCurrentRecordData.Command(actor);
        GetCurrentRecordData.Result result = getCurrentRecordUseCase.execute(command);
        RecordSessionDto.Session response = GetCurrentRecordDto.from(result);

        if (response == null) {
            return ApiResponse.success(null, "현재 기록 세션이 없습니다.");
        }
        return ApiResponse.success(response, "현재 기록 세션을 조회했습니다.");
    }

    @GetMapping("/activities/monitored-apps")
    public ApiResponse<GetMonitoredAppsDto.Response> getMonitoredApps(
        @AuthenticatedActor Actor actor
    ) {
        GetMonitoredAppsData.Command command = new GetMonitoredAppsData.Command(actor);
        GetMonitoredAppsData.Result result = getMonitoredAppsUseCase.execute(command);

        return ApiResponse.success(
            GetMonitoredAppsDto.Response.from(result),
            "활동 기록 가능 앱 목록을 조회했습니다."
        );
    }

    @PatchMapping("/activities/switch-app")
    public ApiResponse<SwitchActivityAppDto.Response> switchActivityApp(
        @Valid @RequestBody SwitchActivityAppDto.Request request,
        @AuthenticatedActor Actor actor
    ) {
        SwitchActivityAppData.Command command = new SwitchActivityAppData.Command(
            actor,
            request.appName()
        );
        SwitchActivityAppData.Result result = switchActivityAppUseCase.execute(command);

        return ApiResponse.success(
            SwitchActivityAppDto.Response.from(result),
            "활동 앱을 전환했습니다."
        );
    }

}
