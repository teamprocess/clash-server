package com.process.clash.application.user.user.exception.exception.validation;

import com.process.clash.application.common.exception.exception.InternalServerException;
import com.process.clash.application.common.exception.statuscode.CommonStatusCode;

public class EntityRefreshFailedException extends InternalServerException {
    public EntityRefreshFailedException() {
        super(CommonStatusCode.ENTITY_REFRESH_FAILED);
    }

    public EntityRefreshFailedException(Throwable cause) {
        super(CommonStatusCode.ENTITY_REFRESH_FAILED, cause);
    }
}
