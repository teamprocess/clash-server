package com.process.clash.application.roadmap.category.port.in;

import com.process.clash.application.roadmap.category.data.CreateCategoryData;

public interface CreateCategoryUseCase {
    CreateCategoryData.Result execute(CreateCategoryData.Command command);
}