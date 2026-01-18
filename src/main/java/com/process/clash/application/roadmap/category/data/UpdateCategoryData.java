package com.process.clash.application.roadmap.category.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.roadmap.entity.Category;

public class UpdateCategoryData {

    public record Command(Actor actor, Long categoryId, String name) {}

    public record Result(Long categoryId, String name, String updatedAt) {
        public static Result from(Category category) {
            String updatedAtString = category.getUpdatedAt() != null
                    ? category.getUpdatedAt().toString()
                    : null;
            return new Result(category.getId(), category.getName(), updatedAtString);
        }
    }
}