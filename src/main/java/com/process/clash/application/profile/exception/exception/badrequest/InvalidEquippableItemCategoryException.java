package com.process.clash.application.profile.exception.exception.badrequest;

import com.process.clash.application.common.exception.exception.BadRequestException;
import com.process.clash.application.profile.exception.statuscode.ProfileStatusCode;

public class InvalidEquippableItemCategoryException extends BadRequestException {
    public InvalidEquippableItemCategoryException() {
        super(ProfileStatusCode.INVALID_EQUIPPABLE_ITEM_CATEGORY);
    }
}
