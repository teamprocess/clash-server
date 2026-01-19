package com.process.clash.adapter.web.shop.season.docs.controller;

import com.process.clash.adapter.web.shop.season.docs.request.CreateSeasonRequestDoc;
import com.process.clash.adapter.web.shop.season.docs.response.CreateSeasonResponseDoc;
import com.process.clash.adapter.web.shop.season.dto.CreateSeasonDto;
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

@Tag(name = "시즌 관리 API", description = "시즌 생성")
public interface SeasonAdminControllerDocument {

    @Operation(summary = "시즌 생성", description = "시즌 정보를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공",
                    content = @Content(
                            schema = @Schema(implementation = CreateSeasonResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "시즌 생성에 성공했습니다."
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<Void> createSeason(
            @Parameter(hidden = true) Actor actor,
            @RequestBody(description = "시즌 생성 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = CreateSeasonRequestDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "name": "2025 봄 시즌",
                                      "startDate": "2025-03-01",
                                      "endDate": "2025-05-31"
                                    }
                                    """)
                    ))
            CreateSeasonDto.Request request
    );
}
