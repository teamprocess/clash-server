package com.process.clash.adapter.web.roadmap.category.docs.controller;

import com.process.clash.adapter.web.roadmap.category.docs.request.CreateCategoryRequestDoc;
import com.process.clash.adapter.web.roadmap.category.docs.request.UpdateCategoryRequestDoc;
import com.process.clash.adapter.web.roadmap.category.docs.response.CreateCategoryResponseDoc;
import com.process.clash.adapter.web.roadmap.category.docs.response.DeleteCategoryResponseDoc;
import com.process.clash.adapter.web.roadmap.category.docs.response.GetCategoriesResponseDoc;
import com.process.clash.adapter.web.roadmap.category.docs.response.UpdateCategoryResponseDoc;
import com.process.clash.adapter.web.roadmap.category.dto.CreateCategoryDto;
import com.process.clash.adapter.web.roadmap.category.dto.DeleteCategoryDto;
import com.process.clash.adapter.web.roadmap.category.dto.GetCategoriesDto;
import com.process.clash.adapter.web.roadmap.category.dto.UpdateCategoryDto;
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
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "카테고리 관리 API", description = "로드맵 카테고리 관리")
public interface AdminCategoryControllerDocument {

    @Operation(summary = "카테고리 생성", description = "로드맵 카테고리를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "생성 성공",
                    content = @Content(
                            schema = @Schema(implementation = CreateCategoryResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "카테고리가 성공적으로 생성되었습니다.",
                                      "data": {
                                        "categoryId": 10,
                                        "name": "백엔드",
                                        "createdAt": "2025-01-01T12:00:00"
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<CreateCategoryDto.Response> createCategory(
            @Parameter(hidden = true) Actor actor,
            @RequestBody(description = "카테고리 생성 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = CreateCategoryRequestDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "name": "백엔드"
                                    }
                                    """)
                    ))
            CreateCategoryDto.Request request
    );

    @Operation(summary = "카테고리 목록 조회", description = "전체 카테고리 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = GetCategoriesResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "카테고리 목록 조회를 성공했습니다.",
                                      "data": {
                                        "categories": [
                                          {
                                            "id": 10,
                                            "name": "백엔드",
                                            "createdAt": "2025-01-01T12:00:00",
                                            "updatedAt": "2025-01-01T12:00:00"
                                          }
                                        ]
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<GetCategoriesDto.Response> getCategories(
            @Parameter(hidden = true) Actor actor
    );

    @Operation(summary = "카테고리 수정", description = "카테고리 이름을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(
                            schema = @Schema(implementation = UpdateCategoryResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "카테고리가 성공적으로 수정되었습니다.",
                                      "data": {
                                        "categoryId": 10,
                                        "name": "백엔드 심화",
                                        "updatedAt": "2025-01-02T09:00:00"
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<UpdateCategoryDto.Response> updateCategory(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "카테고리 ID", example = "1") @PathVariable Long categoryId,
            @RequestBody(description = "카테고리 수정 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = UpdateCategoryRequestDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "name": "백엔드 심화"
                                    }
                                    """)
                    ))
            UpdateCategoryDto.Request request
    );

    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공",
                    content = @Content(
                            schema = @Schema(implementation = DeleteCategoryResponseDoc.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "카테고리가 성공적으로 삭제되었습니다.",
                                      "data": {
                                        "categoryId": 10
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<DeleteCategoryDto.Response> deleteCategory(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "카테고리 ID", example = "1") @PathVariable Long categoryId
    );
}
