package com.process.clash.application.compete.rival.exception.exception;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.compete.rival.exception.statuscode.RivalStatusCode;

public class TooMuchRivalsException extends BadRequestException {
    public TooMuchRivalsException() {
        super(RivalStatusCode.TOO_MUCH_RIVALS);
    }

    public TooMuchRivalsException(Throwable cause) {
        super(RivalStatusCode.TOO_MUCH_RIVALS, cause);
    }
}
