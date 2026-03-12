package com.process.clash.application.compete.rival.rival.exception.exception.badrequet;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.compete.rival.rival.exception.statuscode.RivalStatusCode;

public class TooMuchRivalsException extends BadRequestException {
    public TooMuchRivalsException() {
        super(RivalStatusCode.TOO_MUCH_RIVALS);
    }
}
