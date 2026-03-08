package com.process.clash.adapter.web.compete.rival.battle.docs.controller;

import com.process.clash.adapter.web.compete.rival.battle.docs.request.*;
import com.process.clash.adapter.web.compete.rival.battle.docs.response.*;
import com.process.clash.adapter.web.compete.rival.battle.dto.*;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.common.enums.TargetCategory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "배틀 API", description = "라이벌과의 배틀 신청/관리 및 조회")
public interface BattleControllerDocument {

    @Operation(summary = "배틀 신청", description = "라이벌에게 배틀을 신청합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "신청 성공",
                    content = @Content(
                            schema = @Schema(implementation = ApplyBattleResponseDocument.class),
                            examples = @ExampleObject(value = """
                                  {
                                    "success": true,
                                    "message": "라이벌과의 배틀 신청을 성공적으로 생성하였습니다."
                                  }
                                  """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> applyBattle(
            @Parameter(hidden = true) Actor actor,
            @RequestBody(description = "배틀 신청 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = ApplyBattleRequestDocument.class),
                            examples = @ExampleObject(value = """
                                  {
                                    "id": 3,
                                    "duration": 7
                                  }
                                  """)
                    ))
            ApplyBattleDto.Request request
    );

    @Operation(summary = "배틀 승인", description = "신청받은 배틀을 승인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "승인 성공",
                    content = @Content(
                            schema = @Schema(implementation = ModifyBattleResponseDocument.class),
                            examples = @ExampleObject(value = """
                                  {
                                    "success": true,
                                    "message": "라이벌과의 배틀 신청을 성공적으로 승인하였습니다."
                                  }
                                  """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> acceptBattle(
            @Parameter(hidden = true) Actor actor,
            @RequestBody(description = "배틀 승인 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = ModifyBattleRequestDocument.class),
                            examples = @ExampleObject(value = """
                                  {
                                    "id": 1
                                  }
                                  """)
                    ))
            ModifyBattleDto.Request request
    );

    @Operation(summary = "배틀 취소", description = "내가 신청한 배틀을 취소합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "취소 성공",
                    content = @Content(
                            schema = @Schema(implementation = ModifyBattleResponseDocument.class),
                            examples = @ExampleObject(value = """
                                  {
                                    "success": true,
                                    "message": "라이벌과의 배틀 신청을 성공적으로 취소하였습니다."
                                  }
                                  """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> cancelBattle(
            @Parameter(hidden = true) Actor actor,
            @RequestBody(description = "배틀 취소 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = ModifyBattleRequestDocument.class),
                            examples = @ExampleObject(value = """
                                  {
                                    "id": 1
                                  }
                                  """)
                    ))
            ModifyBattleDto.Request request
    );

    @Operation(summary = "배틀 거절", description = "신청받은 배틀을 거절합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "거절 성공",
                    content = @Content(
                            schema = @Schema(implementation = ModifyBattleResponseDocument.class),
                            examples = @ExampleObject(value = """
                                  {
                                    "success": true,
                                    "message": "라이벌과의 배틀 신청을 성공적으로 거절하였습니다."
                                  }
                                  """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> rejectBattle(
            @Parameter(hidden = true) Actor actor,
            @RequestBody(description = "배틀 거절 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = ModifyBattleRequestDocument.class),
                            examples = @ExampleObject(value = """
                                  {
                                    "id": 1
                                  }
                                  """)
                    ))
            ModifyBattleDto.Request request
    );

    @Operation(summary = "배틀 가능한 라이벌 조회", description = "배틀을 신청할 수 있는 라이벌 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = FindAbleRivalsResponseDocument.class),
                            examples = @ExampleObject(value = """
                                  {
                                    "success": true,
                                    "message": "배틀을 신청할 라이벌 목록을 성공적으로 반환하였습니다.",
                                    "data": {
                                      "rivals": [
                                          {
                                              "id": 1,
                                              "name": "오용준",
                                              "profileImage": "p-i-g"
                                          }
                                      ]
                                    }
                                  }
                                  """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<FindAbleRivalsDto.Response> findAbleRivals(
            @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "배틀 상세 정보 조회", description = "특정 배틀의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = FindDetailedBattleInfoResponseDocument.class),
                            examples = @ExampleObject(value = """
                                  {
                                    "success": true,
                                    "message": "라이벌과의 배틀 상세 정보를 성공적으로 반환했습니다.",
                                    "data": {
                                      "id": 1,
                                      "enemy": {
                                        "id": 3,
                                        "name": "이몽룡",
                                        "profileImage": "https://cdn.example.com/profile/2.png"
                                      },
                                      "expireDate": "2026-01-29",
                                      "myOverallPercentage": 65.5,
                                      "enemyOverallPercentage": 34.5
                                    }
                                  }
                                  """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<FindDetailedBattleInfoDto.Response> findDetailedCertainBattleInfo(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "배틀 ID", example = "1", required = true)
            @PathVariable Long id
    );

    @Operation(summary = "전체 배틀 정보 조회", description = "내가 참여한 모든 배틀 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = FindAllBattleInfoResponseDocument.class),
                            examples = @ExampleObject(value = """
                                  {
                                    "success": true,
                                    "message": "라이벌과의 배틀 정보를 성공적으로 반환했습니다.",
                                    "data": {
                                      "battles": [
                                        {
                                          "id": 3,
                                          "enemy": {
                                              "id": 1,
                                              "name": "오용준",
                                              "profileImage": "p-i-g"
                                          },
                                          "expireDate": "2026-01-29",
                                          "result": "WINNING"
                                        }
                                      ]
                                    }
                                  }
                                  """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<FindAllBattleInfoDto.Response> findAllBattleInfo(
            @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "배틀 정보 분석", description = "특정 배틀의 카테고리별 상세 분석 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = AnalyzeBattleInfoResponseDocument.class),
                            examples = @ExampleObject(value = """
                                  {
                                    "success": true,
                                    "message": "라이벌과의 배틀 정보 분석을 성공적으로 반환했습니다.",
                                    "data": {
                                      "category": "EXP",
                                      "id": 1,
                                      "enemyPoint": 850,
                                      "myPoint": 720
                                    }
                                  }
                                  """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<AnalyzeBattleInfoDto.Response> analyzeBattleInfo(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "배틀 ID", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "분석할 카테고리 (EXP, GITHUB, ACTIVE_TIME, SOLVED_AC)", example = "EXP", required = true)
            @PathVariable TargetCategory category
    );
}