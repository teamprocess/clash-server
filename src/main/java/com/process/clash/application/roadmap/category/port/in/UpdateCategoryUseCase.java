package com.process.clash.application.roadmap.category.port.in;

import com.process.clash.application.roadmap.category.data.UpdateCategoryData;

public interface UpdateCategoryUseCase {
    UpdateCategoryData.Result execute(UpdateCategoryData.Command command);
}