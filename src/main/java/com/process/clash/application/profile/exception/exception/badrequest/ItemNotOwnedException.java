package com.process.clash.application.profile.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.profile.exception.statuscode.ProfileStatusCode;

public class ItemNotOwnedException extends BadRequestException {
    public ItemNotOwnedException() {
        super(ProfileStatusCode.ITEM_NOT_OWNED);
    }
}
