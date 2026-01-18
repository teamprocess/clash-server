package com.process.clash.adapter.web.roadmap.category.dto;

import com.process.clash.application.roadmap.category.data.GetCategoriesData;

import java.util.List;

public class GetCategoriesDto {

    public record Response(List<CategoryVo> categories) {
        public static Response from(GetCategoriesData.Result result) {
            return new Response(result.categories().stream()
                    .map(CategoryVo::from)
                    .toList());
        }
    }

    public record CategoryVo(Long id, String name, String createdAt, String updatedAt) {
        public static CategoryVo from(GetCategoriesData.Result.CategoryVo vo) {
            return new CategoryVo(vo.id(), vo.name(), vo.createdAt(), vo.updatedAt());
        }
    }
}