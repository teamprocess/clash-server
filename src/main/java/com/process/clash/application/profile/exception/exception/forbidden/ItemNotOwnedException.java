package com.process.clash.application.profile.exception.exception.forbidden;

import com.process.clash.application.common.exception.exception.ForbiddenException;
import com.process.clash.application.profile.exception.statuscode.ProfileStatusCode;

public class ItemNotOwnedException extends ForbiddenException {
    public ItemNotOwnedException() {
        super(ProfileStatusCode.ITEM_NOT_OWNED);
    }
}
