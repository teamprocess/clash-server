package com.process.clash.adapter.web.roadmap.category.controller;

import com.process.clash.adapter.web.common.ApiResponse;
import com.process.clash.adapter.web.roadmap.category.dto.CreateCategoryDto;
import com.process.clash.adapter.web.roadmap.category.dto.DeleteCategoryDto;
import com.process.clash.adapter.web.roadmap.category.dto.GetCategoriesDto;
import com.process.clash.adapter.web.roadmap.category.dto.UpdateCategoryDto;
import com.process.clash.adapter.web.security.AuthenticatedActor;
import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.roadmap.category.data.CreateCategoryData;
import com.process.clash.application.roadmap.category.data.DeleteCategoryData;
import com.process.clash.application.roadmap.category.data.GetCategoriesData;
import com.process.clash.application.roadmap.category.data.UpdateCategoryData;
import com.process.clash.application.roadmap.category.port.in.CreateCategoryUseCase;
import com.process.clash.application.roadmap.category.port.in.DeleteCategoryUseCase;
import com.process.clash.application.roadmap.category.port.in.GetCategoriesUseCase;
import com.process.clash.application.roadmap.category.port.in.UpdateCategoryUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoriesUseCase getCategoriesUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    @PostMapping
    public ApiResponse<CreateCategoryDto.Response> createCategory(
            @AuthenticatedActor Actor actor,
            @Valid @RequestBody CreateCategoryDto.Request request
    ) {
        CreateCategoryData.Command command = request.toCommand(actor);
        CreateCategoryData.Result result = createCategoryUseCase.execute(command);
        CreateCategoryDto.Response response = CreateCategoryDto.Response.from(result);
        return ApiResponse.success(response, "카테고리가 성공적으로 생성되었습니다.");
    }

    @GetMapping
    public ApiResponse<GetCategoriesDto.Response> getCategories(
            @AuthenticatedActor Actor actor
    ) {
        GetCategoriesData.Command command = new GetCategoriesData.Command(actor);
        GetCategoriesData.Result result = getCategoriesUseCase.execute(command);
        GetCategoriesDto.Response response = GetCategoriesDto.Response.from(result);
        return ApiResponse.success(response, "카테고리 목록 조회를 성공했습니다.");
    }

    @PutMapping("/{categoryId}")
    public ApiResponse<UpdateCategoryDto.Response> updateCategory(
            @AuthenticatedActor Actor actor,
            @PathVariable Long categoryId,
            @Valid @RequestBody UpdateCategoryDto.Request request
    ) {
        UpdateCategoryData.Command command = request.toCommand(actor, categoryId);
        UpdateCategoryData.Result result = updateCategoryUseCase.execute(command);
        UpdateCategoryDto.Response response = UpdateCategoryDto.Response.from(result);
        return ApiResponse.success(response, "카테고리가 성공적으로 수정되었습니다.");
    }

    @DeleteMapping("/{categoryId}")
    public ApiResponse<DeleteCategoryDto.Response> deleteCategory(
            @AuthenticatedActor Actor actor,
            @PathVariable Long categoryId
    ) {
        DeleteCategoryData.Command command = new DeleteCategoryData.Command(actor, categoryId);
        DeleteCategoryData.Result result = deleteCategoryUseCase.execute(command);
        DeleteCategoryDto.Response response = DeleteCategoryDto.Response.from(result);
        return ApiResponse.success(response, "카테고리가 성공적으로 삭제되었습니다.");
    }
}