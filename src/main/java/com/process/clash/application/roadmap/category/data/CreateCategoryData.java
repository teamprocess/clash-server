package com.process.clash.application.roadmap.category.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.roadmap.entity.Category;
import java.time.Instant;

public class CreateCategoryData {

    public record Command(Actor actor, String name) {}

    public record Result(Long categoryId, String name, Instant createdAt) {
        public static Result from(Category category) {
            return new Result(category.getId(), category.getName(), category.getCreatedAt());
        }
    }
}
