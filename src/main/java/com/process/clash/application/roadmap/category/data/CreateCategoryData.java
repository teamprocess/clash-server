package com.process.clash.application.roadmap.category.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.roadmap.entity.Category;

public class CreateCategoryData {

    public record Command(Actor actor, String name) {}

    public record Result(Long categoryId, String name, String createdAt) {
        public static Result from(Category category) {
            String createdAtString = category.getCreatedAt() != null
                    ? category.getCreatedAt().toString()
                    : null;
            return new Result(category.getId(), category.getName(), createdAtString);
        }
    }
}