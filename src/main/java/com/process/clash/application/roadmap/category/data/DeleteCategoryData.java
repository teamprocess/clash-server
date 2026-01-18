package com.process.clash.application.roadmap.category.data;

import com.process.clash.application.common.actor.Actor;

public class DeleteCategoryData {

    public record Command(Actor actor, Long categoryId) {}

    public record Result(Long categoryId) {}
}