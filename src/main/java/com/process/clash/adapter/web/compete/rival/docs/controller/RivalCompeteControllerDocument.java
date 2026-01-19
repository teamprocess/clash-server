package com.process.clash.adapter.web.compete.rival.docs.controller;

import com.process.clash.adapter.web.compete.rival.docs.request.ApplyRivalRequestDoc;
import com.process.clash.adapter.web.compete.rival.docs.response.ApplyRivalResponseDoc;
import com.process.clash.adapter.web.compete.rival.docs.response.GetAllAbleRivalsResponseDoc;
import com.process.clash.adapter.web.compete.rival.docs.response.GetMyRivalActingResponseDoc;
import com.process.clash.adapter.web.compete.rival.docs.response.SearchRivalByKeywordResponseDoc;
import com.process.clash.adapter.web.compete.rival.dto.ApplyRivalDto;
import com.process.clash.adapter.web.compete.rival.dto.GetAllAbleRivalsDto;
import com.process.clash.adapter.web.compete.rival.dto.GetMyRivalActingDto;
import com.process.clash.adapter.web.compete.rival.dto.SearchRivalByKeywordDto;
import com.process.clash.application.common.actor.Actor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "라이벌 API", description = "라이벌 조회/등록")
public interface RivalCompeteControllerDocument {

    @Operation(summary = "내 라이벌 조회", description = "등록된 라이벌의 현재 상태를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetMyRivalActingResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "라이벌의 현재 상태 정보를 성공적으로 반환했습니다.",
                                      "data": {
                                        "myRivals": [
                                          {
                                            "name": "이몽룡",
                                            "username": "mongryong",
                                            "profileImage": "https://cdn.example.com/profile/2.png",
                                            "activeTime": "02:30",
                                            "usingApp": "CLASH",
                                            "status": "STUDYING"
                                          }
                                        ]
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetMyRivalActingDto.Response> getMyRivalActing(
            @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "라이벌 후보 조회", description = "라이벌 등록 가능한 유저 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetAllAbleRivalsResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "라이벌 등록 가능한 유저 목록을 성공적으로 조회했습니다.",
                                      "data": {
                                        "users": [
                                          {
                                            "id": 3,
                                            "name": "성춘향",
                                            "githubId": "chunhyang"
                                          }
                                        ]
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetAllAbleRivalsDto.Response> getAllAbleRivals(
            @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "라이벌 검색", description = "키워드로 라이벌 등록 가능한 유저를 검색합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = SearchRivalByKeywordResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "키워드를 이용하여 라이벌 등록가능한 유저를 성공적으로 조회했습니다.",
                                      "data": {
                                        "users": [
                                          {
                                            "id": 3,
                                            "name": "성춘향",
                                            "githubId": "chunhyang"
                                          }
                                        ]
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<SearchRivalByKeywordDto.Response> searchRivalByKeyword(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "검색 키워드", example = "홍길동", required = true)
            @RequestParam String keyword
    );

    @Operation(summary = "라이벌 신청", description = "라이벌로 추가할 유저를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "등록 성공",
                    content = @Content(
                            schema = @Schema(implementation = ApplyRivalResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "라이벌을 성공적으로 추가했습니다."
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> applyRival(
            @Parameter(hidden = true) Actor actor,
            @RequestBody(description = "라이벌 신청 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = ApplyRivalRequestDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "ids": [
                                        { "id": 3 },
                                        { "id": 4 }
                                      ]
                                    }
                                    """)
                    ))
            ApplyRivalDto.Request request
    );
}
