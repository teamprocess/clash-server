package com.process.clash.adapter.web.compete.rival.rival.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.compete.rival.rival.docs.controller.RivalCompeteControllerDocument;
import com.process.clash.adapter.web.compete.rival.rival.dto.*;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.compete.rival.rival.data.*;
import com.process.clash.application.compete.rival.rival.port.in.*;
import com.process.clash.domain.common.enums.PeriodCategory;
import com.process.clash.domain.common.enums.TargetCategory;
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
    private final AcceptRivalUseCase acceptRivalUseCase;
    private final RejectRivalUseCase rejectRivalUseCase;
    private final CancelRivalUseCase cancelRivalUseCase;
    private final RemoveRivalUseCase removeRivalUseCase;
    private final CompareWithRivalsUseCase compareWithRivalsUseCase;

    // 내 라이벌 정보 조회
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

        SearchRivalByKeywordData.Command command = SearchRivalByKeywordData.Command.of(actor, keyword);
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

    // 라이벌 - 라이벌 승인
    @PostMapping("/accept")
    public ApiResponse<Void> acceptRival(
            @AuthenticatedActor Actor actor,
            @Valid @RequestBody ModifyRivalDto.Request request
    ) {

        ModifyRivalData.Command command = request.toCommand(actor);
        acceptRivalUseCase.execute(command);
        return ApiResponse.success("라이벌을 성공적으로 승인했습니다.");
    }

    // 라이벌 - 라이벌 거절
    @PostMapping("/reject")
    public ApiResponse<Void> rejectRival(
            @AuthenticatedActor Actor actor,
            @Valid @RequestBody ModifyRivalDto.Request request
    ) {

        ModifyRivalData.Command command = request.toCommand(actor);
        rejectRivalUseCase.execute(command);
        return ApiResponse.success("라이벌을 성공적으로 거절했습니다.");
    }

    // 라이벌 - 라이벌 신청 취소
    @PostMapping("/cancel")
    public ApiResponse<Void> cancelRival(
            @AuthenticatedActor Actor actor,
            @Valid @RequestBody ModifyRivalDto.Request request
    ) {

        ModifyRivalData.Command command = request.toCommand(actor);
        cancelRivalUseCase.execute(command);
        return ApiResponse.success("라이벌 신청을 성공적으로 취소했습니다.");
    }

    // 라이벌 - 라이벌 삭제
    @DeleteMapping("/remove/{id}")
    public ApiResponse<Void> removeRival(
            @AuthenticatedActor Actor actor,
            @PathVariable Long id
    ) {

        ModifyRivalData.Command command = ModifyRivalData.Command.of(actor, id);
        removeRivalUseCase.execute(command);
        return ApiResponse.success("라이벌을 성공적으로 삭제했습니다.");
    }

    // 라이벌과의 경쟁 - 라이벌과의 비교
    @GetMapping("/compare/category/{category}/period/{period}")
    public ApiResponse<CompareWithRivalsDto.Response> compareWithRivals(
            @AuthenticatedActor Actor actor,
            @PathVariable TargetCategory category,
            @PathVariable PeriodCategory period
    ) {

        CompareWithRivalsData.Command command = CompareWithRivalsData.Command.of(actor, category, period);
        CompareWithRivalsData.Result result = compareWithRivalsUseCase.execute(command);
        CompareWithRivalsDto.Response response = CompareWithRivalsDto.Response.from(result);
        return ApiResponse.success(response, "라이벌과의 비교 정보를 성공적으로 반환했습니다.");
    }
}
