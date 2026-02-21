package com.process.clash.application.roadmap.category.port.in;

import com.process.clash.application.roadmap.category.data.UpdateCategoryImageData;

public interface UpdateCategoryImageUseCase {
    UpdateCategoryImageData.Result execute(UpdateCategoryImageData.Command command);
}
