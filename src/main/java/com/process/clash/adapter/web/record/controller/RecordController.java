package com.process.clash.adapter.web.record.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.record.docs.controller.RecordControllerDocument;
import com.process.clash.adapter.web.record.dto.GetTodayRecordDto;
import com.process.clash.adapter.web.record.dto.RecordSettingDto;
import com.process.clash.adapter.web.record.dto.StartRecordDto;
import com.process.clash.adapter.web.record.dto.StopRecordDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.dto.GetRecordSettingData;
import com.process.clash.application.record.dto.GetTodayRecordData;
import com.process.clash.application.record.dto.StartRecordData;
import com.process.clash.application.record.dto.StopRecordData;
import com.process.clash.application.record.dto.UpdateRecordSettingData;
import com.process.clash.application.record.port.in.GetRecordSettingUseCase;
import com.process.clash.application.record.port.in.GetTodayRecordUseCase;
import com.process.clash.application.record.port.in.StartRecordUseCase;
import com.process.clash.application.record.port.in.StopRecordUseCase;
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
            "오늘의 일반 기록 현황을 조회했습니다."
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
                request.taskId(),
                actor
        );

        StartRecordData.Result result = startRecordUseCase.execute(command);

        return ApiResponse.success(
                StartRecordDto.Response.from(
                        result
                ),
                "일반 기록을 시작했습니다."
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
            "일반 기록을 중지했습니다."
        );
    }

}
