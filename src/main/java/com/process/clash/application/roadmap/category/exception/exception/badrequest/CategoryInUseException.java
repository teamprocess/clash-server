package com.process.clash.application.roadmap.category.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.roadmap.category.exception.status.CategoryStatusCode;

public class CategoryInUseException extends BadRequestException {
    public CategoryInUseException() {
        super(CategoryStatusCode.CATEGORY_IN_USE);
    }
}