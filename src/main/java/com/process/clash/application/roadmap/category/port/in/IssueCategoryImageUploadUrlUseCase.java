package com.process.clash.application.roadmap.category.port.in;

import com.process.clash.application.roadmap.category.data.IssueCategoryImageUploadUrlData;

public interface IssueCategoryImageUploadUrlUseCase {
    IssueCategoryImageUploadUrlData.Result execute(IssueCategoryImageUploadUrlData.Command command);
}
