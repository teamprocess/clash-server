package com.process.clash.application.user.user.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.user.user.exception.statuscode.UserStatusCode;

public class InvalidProfileImageUploadRequestException extends BadRequestException {

    public InvalidProfileImageUploadRequestException() {
        super(UserStatusCode.INVALID_PROFILE_IMAGE_UPLOAD_REQUEST);
    }

    public InvalidProfileImageUploadRequestException(Throwable cause) {
        super(UserStatusCode.INVALID_PROFILE_IMAGE_UPLOAD_REQUEST, cause);
    }
}
