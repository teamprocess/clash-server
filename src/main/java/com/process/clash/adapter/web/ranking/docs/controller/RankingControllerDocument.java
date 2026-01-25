package com.process.clash.adapter.web.ranking.docs.controller;

import com.process.clash.adapter.web.ranking.docs.response.GetChapterRankingResponseDoc;
import com.process.clash.adapter.web.ranking.docs.response.GetRankingResponseDoc;
import com.process.clash.adapter.web.ranking.dto.GetChapterRankingDto;
import com.process.clash.adapter.web.ranking.dto.GetRankingDto;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.common.enums.PeriodCategory;
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

@Tag(name = "랭킹 API", description = "카테고리/기간별 랭킹")
public interface RankingControllerDocument {

    @Operation(summary = "랭킹 조회", description = "카테고리와 기간 기준 랭킹을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetRankingResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "내 EXP 획득 정보 분석 결과를 성공적으로 반환했습니다.",
                                      "data": {
                                        "category": "SERVER",
                                        "period": "WEEK",
                                        "rankings": [
                                          {
                                            "name": "홍길동",
                                            "profileImage": "https://cdn.example.com/profile/1.png",
                                            "isRival": true,
                                            "linkedId": "github",
                                            "point": 1200
                                          },
                                          {
                                            "name": "김철수",
                                            "profileImage": "https://cdn.example.com/profile/2.png",
                                            "isRival": false,
                                            "linkedId": "gitlab",
                                            "point": 980
                                          }
                                        ]
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetRankingDto.Response> getRanking(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "카테고리", example = "SERVER", required = true)
            @PathVariable TargetCategory category,
            @Parameter(description = "기간", example = "WEEK", required = true)
            @PathVariable PeriodCategory period
    );

    @Operation(summary = "챕터 랭킹 조회", description = "챕터 완료 수 랭킹을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetChapterRankingResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "챕터 완료 수 랭킹 조회를 성공했습니다.",
                                      "data": {
                                        "myRank": {
                                          "rank": 5,
                                          "completedChaptersCount": 28,
                                          "id": 100,
                                          "name": "나",
                                          "profileImage": "https://cdn.example.com/profile/me.png"
                                        },
                                        "allRankers": [
                                          {
                                            "rank": 1,
                                            "completedChaptersCount": 42,
                                            "id": 1,
                                            "name": "홍길동",
                                            "profileImage": "https://cdn.example.com/profile/1.png"
                                          },
                                          {
                                            "rank": 2,
                                            "completedChaptersCount": 35,
                                            "id": 2,
                                            "name": "김철수",
                                            "profileImage": "https://cdn.example.com/profile/2.png"
                                          }
                                        ]
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetChapterRankingDto.Response> getChapterRanking(
            @Parameter(hidden = true) Actor actor
    );
}