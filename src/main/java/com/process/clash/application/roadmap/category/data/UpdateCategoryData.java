package com.process.clash.application.roadmap.category.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.roadmap.entity.Category;
import java.time.Instant;

public class UpdateCategoryData {

    public record Command(Actor actor, Long categoryId, String name) {}

    public record Result(Long categoryId, String name, Instant updatedAt) {
        public static Result from(Category category) {
            return new Result(category.getId(), category.getName(), category.getUpdatedAt());
        }
    }
}
