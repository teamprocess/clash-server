package com.process.clash.application.roadmap.category.data;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.domain.roadmap.entity.Category;

public final class UpdateCategoryImageData {

    private UpdateCategoryImageData() {}

    public record Command(
            Actor actor,
            Long categoryId,
            String imageUrl
    ) {}

    public record Result(Long categoryId, String imageUrl) {
        public static Result from(Category category) {
            return new Result(category.getId(), category.getImageUrl());
        }
    }
}
