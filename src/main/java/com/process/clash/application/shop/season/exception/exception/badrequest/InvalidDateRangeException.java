package com.process.clash.application.shop.season.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.shop.season.exception.status.SeasonStatusCode;

public class InvalidDateRangeException extends BadRequestException {
    public InvalidDateRangeException() {
        super(SeasonStatusCode.INVALID_DATE_RANGE);
    }

    public InvalidDateRangeException(Throwable cause) {
        super(SeasonStatusCode.INVALID_DATE_RANGE, cause);
    }
}
