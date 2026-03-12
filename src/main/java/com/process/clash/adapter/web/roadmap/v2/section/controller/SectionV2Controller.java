package com.process.clash.adapter.web.roadmap.v2.section.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.roadmap.v2.section.docs.controller.SectionV2ControllerDocument;
import com.process.clash.adapter.web.roadmap.v2.section.dto.GetSectionV2ListDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.v2.section.data.GetSectionV2ListData;
import com.process.clash.application.roadmap.v2.section.port.in.GetSectionV2ListUseCase;
import com.process.clash.domain.common.enums.Major;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/sections")
@RequiredArgsConstructor
public class SectionV2Controller implements SectionV2ControllerDocument {

    private final GetSectionV2ListUseCase getSectionV2ListUseCase;

    @GetMapping
    public ApiResponse<GetSectionV2ListDto.Response> getSections(
            @AuthenticatedActor Actor actor,
            @RequestParam Major major
    ) {
        GetSectionV2ListData.Command command = new GetSectionV2ListData.Command(actor, major);
        GetSectionV2ListData.Result result = getSectionV2ListUseCase.execute(command);
        GetSectionV2ListDto.Response response = GetSectionV2ListDto.Response.from(result);
        return ApiResponse.success(response, "로드맵 목록 조회를 성공했습니다.");
    }
}
