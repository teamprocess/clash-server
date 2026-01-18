package com.process.clash.application.roadmap.category.port.in;

import com.process.clash.application.roadmap.category.data.DeleteCategoryData;

public interface DeleteCategoryUseCase {
    DeleteCategoryData.Result execute(DeleteCategoryData.Command command);
}