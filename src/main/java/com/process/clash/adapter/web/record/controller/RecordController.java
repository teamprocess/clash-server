package com.process.clash.adapter.web.record.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.record.dto.GetTodayRecordDto;
import com.process.clash.adapter.web.record.dto.StartRecordDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.record.dto.GetTodayRecordData;
import com.process.clash.application.record.dto.StartRecordData;
import com.process.clash.application.record.port.in.GetTodayRecordUseCase;
import com.process.clash.application.record.port.in.StartRecordUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
public class RecordController {

    private final GetTodayRecordUseCase getTodayRecordUseCase;
    private final StartRecordUseCase startRecordUseCase;

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

}
