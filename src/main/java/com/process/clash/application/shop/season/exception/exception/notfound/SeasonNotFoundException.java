package com.process.clash.application.shop.season.exception.exception.notfound;

import com.process.clash.application.common.exception.exception.NotFoundException;
import com.process.clash.application.shop.season.exception.status.SeasonStatusCode;

public class SeasonNotFoundException extends NotFoundException {
    public SeasonNotFoundException() {
        super(SeasonStatusCode.SEASON_NOT_FOUND);
    }

    public SeasonNotFoundException(Throwable cause) {
        super(SeasonStatusCode.SEASON_NOT_FOUND, cause);
    }
}