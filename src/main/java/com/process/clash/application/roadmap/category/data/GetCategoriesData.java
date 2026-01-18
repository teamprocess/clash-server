package com.process.clash.application.roadmap.category.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.roadmap.entity.Category;

import java.util.List;

public class GetCategoriesData {

    public record Command(Actor actor) {}

    public record Result(List<CategoryVo> categories) {
        public static Result from(List<Category> categories) {
            List<CategoryVo> categoryVos = categories.stream()
                    .map(CategoryVo::from)
                    .toList();
            return new Result(categoryVos);
        }

        public record CategoryVo(Long id, String name, String createdAt, String updatedAt) {
            public static CategoryVo from(Category category) {
                String createdAtString = category.getCreatedAt() != null
                        ? category.getCreatedAt().toString()
                        : null;
                String updatedAtString = category.getUpdatedAt() != null
                        ? category.getUpdatedAt().toString()
                        : null;
                return new CategoryVo(category.getId(), category.getName(), createdAtString, updatedAtString);
            }
        }
    }
}