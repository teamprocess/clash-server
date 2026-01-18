package com.process.clash.application.roadmap.category.port.in;

import com.process.clash.application.roadmap.category.data.GetCategoriesData;

public interface GetCategoriesUseCase {
    GetCategoriesData.Result execute(GetCategoriesData.Command command);
}