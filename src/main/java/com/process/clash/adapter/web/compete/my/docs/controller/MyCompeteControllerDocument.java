package com.process.clash.adapter.web.compete.my.docs.controller;

import com.process.clash.adapter.web.compete.my.docs.response.AnalyzeMyActivityResponseDocument;
import com.process.clash.adapter.web.compete.my.docs.response.CompareGitHubResponseDocument;
import com.process.clash.adapter.web.compete.my.docs.response.CompareMyActivityResponseDocument;
import com.process.clash.adapter.web.compete.my.docs.response.GetCompareWithYesterdayResponseDocument;
import com.process.clash.adapter.web.compete.my.docs.response.GetMyGrowthRateResponseDocument;
import com.process.clash.adapter.web.compete.my.dto.AnalyzeMyActivityDto;
import com.process.clash.adapter.web.compete.my.dto.CompareGitHubDto;
import com.process.clash.adapter.web.compete.my.dto.CompareMyActivityDto;
import com.process.clash.adapter.web.compete.my.dto.GetCompareWithYesterdayDto;
import com.process.clash.adapter.web.compete.my.dto.GetMyGrowthRateDto;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.common.enums.TargetCategory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "내 활동 API", description = "내 활동 분석/비교")
public interface MyCompeteControllerDocument {

    @Operation(summary = "어제와 비교", description = "어제 대비 활동 비교 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetCompareWithYesterdayResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "어제와의 비교 정보를 성공적으로 반환했습니다.",
                                      "data": {
                                        "activeTime": {
                                          "yesterdayActiveTime": 120,
                                          "todayActiveTime": 150
                                        },
                                        "contributors": {
                                          "yesterdayContributors": 3,
                                          "todayContributors": 5
                                        }
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetCompareWithYesterdayDto.Response> getCompareWithYesterday(
            @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "어제와 비교 - GitHub", description = "어제 대비 GitHub 활동을 비교합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = CompareGitHubResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "어제와 비교한 유저의 깃허브 정보 조회를 성공적으로 완료 했습니다.",
                                      "data": {
                                        "yesterday": {
                                          "date": "2025-01-01",
                                          "commit": {
                                            "count": 4,
                                            "representationRepo": "clash-server",
                                            "addLines": 120,
                                            "removeLines": 20,
                                            "firstCommit": "2025-01-01T09:00:00",
                                            "lastCommit": "2025-01-01T21:30:00"
                                          },
                                          "pullRequest": {
                                            "count": 1,
                                            "representationRepo": "clash-server",
                                            "mergedCount": 1,
                                            "openCount": 0,
                                            "closedCount": 0,
                                            "inReviewCount": 0,
                                            "approvedCount": 0,
                                            "requestCount": 0
                                          },
                                          "issue": {
                                            "count": 0
                                          },
                                          "review": {
                                            "count": 1
                                          }
                                        },
                                        "today": {
                                          "date": "2025-01-02",
                                          "commit": {
                                            "count": 2,
                                            "representationRepo": "clash-server",
                                            "addLines": 60,
                                            "removeLines": 10,
                                            "firstCommit": "2025-01-02T10:00:00",
                                            "lastCommit": "2025-01-02T18:20:00"
                                          },
                                          "pullRequest": {
                                            "count": 0,
                                            "representationRepo": "clash-server",
                                            "mergedCount": 0,
                                            "openCount": 0,
                                            "closedCount": 0,
                                            "inReviewCount": 0,
                                            "approvedCount": 0,
                                            "requestCount": 0
                                          },
                                          "issue": {
                                            "count": 1
                                          },
                                          "review": {
                                            "count": 0
                                          }
                                        }
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<CompareGitHubDto.Response> compareGitHub(
            @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "내 활동 분석", description = "카테고리별 활동을 분석합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = AnalyzeMyActivityResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "내 활동 분석 결과를 성공적으로 반환했습니다.",
                                      "data": {
                                        "category": "GITHUB",
                                        "streaks": [
                                          {
                                            "date": "2025-01-01",
                                            "detailedInfo": 120
                                          }
                                        ],
                                        "variations": [
                                          {
                                            "month": 1,
                                            "avgVariationPerMonth": 12.5
                                          }
                                        ]
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<AnalyzeMyActivityDto.Response> analyzeMyActivity(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "활동 카테고리 (GITHUB, SOLVED_AC, ACTIVE_TIME, EXP)", example = "GITHUB", required = true)
            @PathVariable TargetCategory category
    );

    @Operation(summary = "내 성장도 분석", description = "기준별 성장도 데이터를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetMyGrowthRateResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "성장도 분석 결과를 성공적으로 반환했습니다.",
                                      "data": {
                                        "dataPoint": [
                                          {
                                            "date": "2025-01-01",
                                            "rate": 15.5
                                          },
                                          {
                                            "date": "2025-01-02",
                                            "rate": 18.2
                                          },
                                          {
                                            "date": "2025-01-03",
                                            "rate": 20.1
                                          }
                                        ]
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetMyGrowthRateDto.Response> getMyGrowthRate(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "조회 기준 (DAY, WEEK, MONTH)", example = "WEEK", required = true)
            @RequestParam String standard
    );

    @Operation(summary = "내 기록 비교", description = "기준별 활동 기록을 비교합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = CompareMyActivityResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "내 기록을 성공적으로 반환했습니다.",
                                      "data": {
                                        "earnedExp": 1250.5,
                                        "studyTime": 480.0,
                                        "gitHubAttribution": 85.3
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<CompareMyActivityDto.Response> compareMyActivity(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "비교 기준 (TODAY, YESTERDAY, LAST_WEEK, LAST_MONTH)", example = "LAST_WEEK", required = true)
            @RequestParam String standard
    );
}