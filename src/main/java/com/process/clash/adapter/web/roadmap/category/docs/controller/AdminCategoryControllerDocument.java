package com.process.clash.adapter.web.roadmap.category.docs.controller;

import com.process.clash.adapter.web.roadmap.category.docs.request.CreateCategoryRequestDocument;
import com.process.clash.adapter.web.roadmap.category.docs.request.IssueCategoryImageUploadUrlRequestDocument;
import com.process.clash.adapter.web.roadmap.category.docs.request.UpdateCategoryImageRequestDocument;
import com.process.clash.adapter.web.roadmap.category.docs.request.UpdateCategoryRequestDocument;
import com.process.clash.adapter.web.roadmap.category.docs.response.CreateCategoryResponseDocument;
import com.process.clash.adapter.web.roadmap.category.docs.response.DeleteCategoryResponseDocument;
import com.process.clash.adapter.web.roadmap.category.docs.response.GetCategoriesResponseDocument;
import com.process.clash.adapter.web.roadmap.category.docs.response.IssueCategoryImageUploadUrlResponseDocument;
import com.process.clash.adapter.web.roadmap.category.docs.response.UpdateCategoryImageResponseDocument;
import com.process.clash.adapter.web.roadmap.category.docs.response.UpdateCategoryResponseDocument;
import com.process.clash.adapter.web.roadmap.category.dto.CreateCategoryDto;
import com.process.clash.adapter.web.roadmap.category.dto.DeleteCategoryDto;
import com.process.clash.adapter.web.roadmap.category.dto.GetCategoriesDto;
import com.process.clash.adapter.web.roadmap.category.dto.IssueCategoryImageUploadUrlDto;
import com.process.clash.adapter.web.roadmap.category.dto.UpdateCategoryDto;
import com.process.clash.adapter.web.roadmap.category.dto.UpdateCategoryImageDto;
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
                            schema = @Schema(implementation = CreateCategoryResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "카테고리가 성공적으로 생성되었습니다.",
                                      "data": {
                                        "categoryId": 10,
                                        "name": "백엔드",
                                        "createdAt": "2025-01-01T12:00:00Z"
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<CreateCategoryDto.Response> createCategory(
            @Parameter(hidden = true) Actor actor,
            @RequestBody(description = "카테고리 생성 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = CreateCategoryRequestDocument.class),
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
                            schema = @Schema(implementation = GetCategoriesResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "카테고리 목록 조회를 성공했습니다.",
                                      "data": {
                                        "categories": [
                                          {
                                            "id": 10,
                                            "name": "백엔드",
                                            "createdAt": "2025-01-01T12:00:00Z",
                                            "updatedAt": "2025-01-01T12:00:00Z"
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
                            schema = @Schema(implementation = UpdateCategoryResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "카테고리가 성공적으로 수정되었습니다.",
                                      "data": {
                                        "categoryId": 10,
                                        "name": "백엔드 심화",
                                        "updatedAt": "2025-01-02T09:00:00Z"
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
                            schema = @Schema(implementation = UpdateCategoryRequestDocument.class),
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
                            schema = @Schema(implementation = DeleteCategoryResponseDocument.class),
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

    @Operation(summary = "카테고리 이미지 업로드 URL 발급", description = "S3 presigned URL을 발급합니다. 프론트는 반환된 uploadUrl로 직접 이미지를 업로드한 뒤 fileUrl을 이미지 저장 API에 전달해야 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "발급 성공",
                    content = @Content(
                            schema = @Schema(implementation = IssueCategoryImageUploadUrlResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "카테고리 이미지 업로드 URL이 발급되었습니다.",
                                      "data": {
                                        "uploadUrl": "https://bucket.s3.amazonaws.com/categories/images/category-1/abc123.png?X-Amz-Signature=...",
                                        "objectKey": "categories/images/category-1/abc123.png",
                                        "fileUrl": "https://bucket.s3.ap-northeast-2.amazonaws.com/categories/images/category-1/abc123.png",
                                        "httpMethod": "PUT",
                                        "contentType": "image/png",
                                        "expiresInSeconds": 300
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<IssueCategoryImageUploadUrlDto.Response> issueCategoryImageUploadUrl(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "카테고리 ID", example = "1") @PathVariable Long categoryId,
            @RequestBody(description = "업로드 URL 발급 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = IssueCategoryImageUploadUrlRequestDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "fileName": "image.png",
                                      "contentType": "image/png"
                                    }
                                    """)
                    ))
            IssueCategoryImageUploadUrlDto.Request request
    );

    @Operation(summary = "카테고리 이미지 저장", description = "S3 업로드 완료 후 이미지 URL을 카테고리에 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "저장 성공",
                    content = @Content(
                            schema = @Schema(implementation = UpdateCategoryImageResponseDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "message": "카테고리 이미지가 성공적으로 업데이트되었습니다.",
                                      "data": {
                                        "categoryId": 1,
                                        "imageUrl": "https://bucket.s3.ap-northeast-2.amazonaws.com/categories/images/category-1/abc123.png"
                                      }
                                    }
                                    """)
                    ))
    })
    com.process.clash.adapter.web.common.ApiResponse<UpdateCategoryImageDto.Response> updateCategoryImage(
            @Parameter(hidden = true) Actor actor,
            @Parameter(description = "카테고리 ID", example = "1") @PathVariable Long categoryId,
            @RequestBody(description = "이미지 URL 저장 요청", required = true,
                    content = @Content(
                            schema = @Schema(implementation = UpdateCategoryImageRequestDocument.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "imageUrl": "https://bucket.s3.ap-northeast-2.amazonaws.com/categories/images/category-1/abc123.png"
                                    }
                                    """)
                    ))
            UpdateCategoryImageDto.Request request
    );
}
