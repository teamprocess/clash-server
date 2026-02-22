package com.process.clash.application.roadmap.category.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.roadmap.category.exception.status.CategoryStatusCode;

public class InvalidCategoryImageUploadRequestException extends BadRequestException {

    public InvalidCategoryImageUploadRequestException() {
        super(CategoryStatusCode.INVALID_CATEGORY_IMAGE_UPLOAD_REQUEST);
    }
}
