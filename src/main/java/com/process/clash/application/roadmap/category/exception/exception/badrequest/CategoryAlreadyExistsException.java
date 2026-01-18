package com.process.clash.application.roadmap.category.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.roadmap.category.exception.status.CategoryStatusCode;

public class CategoryAlreadyExistsException extends BadRequestException {
    public CategoryAlreadyExistsException() {
        super(CategoryStatusCode.CATEGORY_ALREADY_EXISTS);
    }
}