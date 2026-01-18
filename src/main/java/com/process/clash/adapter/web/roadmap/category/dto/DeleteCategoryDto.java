package com.process.clash.adapter.web.roadmap.category.dto;

import com.process.clash.application.roadmap.category.data.DeleteCategoryData;

public class DeleteCategoryDto {

    public record Response(Long categoryId) {
        public static Response from(DeleteCategoryData.Result result) {
            return new Response(result.categoryId());
        }
    }
}