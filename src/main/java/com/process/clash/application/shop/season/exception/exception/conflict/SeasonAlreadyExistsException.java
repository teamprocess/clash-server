package com.process.clash.application.shop.season.exception.exception.conflict;

import com.process.clash.application.common.exception.exception.ConflictException;
import com.process.clash.application.shop.season.exception.status.SeasonStatusCode;

public class SeasonAlreadyExistsException extends ConflictException {
    public SeasonAlreadyExistsException() {
        super(SeasonStatusCode.SEASON_ALREADY_EXISTS);
    }
}
