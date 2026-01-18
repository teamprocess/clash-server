package com.process.clash.application.roadmap.category.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.roadmap.category.exception.status.CategoryStatusCode;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException() {
        super(CategoryStatusCode.CATEGORY_NOT_FOUND);
    }
}