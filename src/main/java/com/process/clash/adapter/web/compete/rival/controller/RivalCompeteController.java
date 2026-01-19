package com.process.clash.adapter.web.compete.rival.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.compete.rival.docs.controller.RivalCompeteControllerDocument;
import com.process.clash.adapter.web.compete.rival.dto.GetMyRivalActingDto;
import com.process.clash.adapter.web.compete.rival.dto.ApplyRivalDto;
import com.process.clash.adapter.web.compete.rival.dto.GetAllAbleRivalsDto;
import com.process.clash.adapter.web.compete.rival.dto.SearchRivalByKeywordDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.data.GetMyRivalActingData;
import com.process.clash.application.compete.rival.data.ApplyRivalData;
import com.process.clash.application.compete.rival.data.GetAllAbleRivalsData;
import com.process.clash.application.compete.rival.data.SearchRivalByKeywordData;
import com.process.clash.application.compete.rival.port.in.GetMyRivalActingUseCase;
import com.process.clash.application.compete.rival.port.in.ApplyRivalUseCase;
import com.process.clash.application.compete.rival.port.in.GetAllAbleRivalsUseCase;
import com.process.clash.application.compete.rival.port.in.SearchRivalByKeywordUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compete/rivals")
@RequiredArgsConstructor
public class RivalCompeteController implements RivalCompeteControllerDocument {

    private final GetMyRivalActingUseCase getMyRivalActingUseCase;
    private final GetAllAbleRivalsUseCase getAllAbleRivalsUseCase;
    private final SearchRivalByKeywordUseCase searchRivalByKeywordUseCase;
    private final ApplyRivalUseCase applyRivalUseCase;

    // 내 라이벌 정보 조회
    // TODO: 추가 구현 필요합니다
    @GetMapping
    public ApiResponse<GetMyRivalActingDto.Response> getMyRivalActing(
            @AuthenticatedActor Actor actor
    ) {

        GetMyRivalActingData.Command command = GetMyRivalActingData.Command.from(actor);
        GetMyRivalActingData.Result result = getMyRivalActingUseCase.execute(command);
        GetMyRivalActingDto.Response response = GetMyRivalActingDto.Response.from(result);
        return ApiResponse.success(response, "라이벌의 현재 상태 정보를 성공적으로 반환했습니다.");
    }

    // 라이벌 - 전체 유저 조회(내 라이벌 제외)
    @GetMapping("/available")
    public ApiResponse<GetAllAbleRivalsDto.Response> getAllAbleRivals(
            @AuthenticatedActor Actor actor
    ) {

        GetAllAbleRivalsData.Command command = GetAllAbleRivalsData.Command.from(actor);
        GetAllAbleRivalsData.Result result = getAllAbleRivalsUseCase.execute(command);
        GetAllAbleRivalsDto.Response response = GetAllAbleRivalsDto.Response.from(result);
        return ApiResponse.success(response, "라이벌 등록 가능한 유저 목록을 성공적으로 조회했습니다.");
    }

    // 라이벌 - 키워드 검색
    @GetMapping("/available/search")
    public ApiResponse<SearchRivalByKeywordDto.Response> searchRivalByKeyword(
            @AuthenticatedActor Actor actor,
            @RequestParam String keyword
    ) {

        SearchRivalByKeywordData.Command command = SearchRivalByKeywordData.Command.from(actor, keyword);
        SearchRivalByKeywordData.Result result = searchRivalByKeywordUseCase.execute(command);
        SearchRivalByKeywordDto.Response response = SearchRivalByKeywordDto.Response.from(result);
        return ApiResponse.success(response, "키워드를 이용하여 라이벌 등록가능한 유저를 성공적으로 조회했습니다.");
    }

    // 라이벌 - 라이벌 신청
    @PostMapping("/apply")
    public ApiResponse<Void> applyRival(
            @AuthenticatedActor Actor actor,
            @Valid @RequestBody ApplyRivalDto.Request request
    ) {

        ApplyRivalData.Command command = request.toCommand(actor);
        applyRivalUseCase.execute(command);
        return ApiResponse.created("라이벌을 성공적으로 추가했습니다.");
    }
}
