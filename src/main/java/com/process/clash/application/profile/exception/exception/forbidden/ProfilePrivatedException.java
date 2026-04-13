package com.process.clash.application.profile.exception.exception.forbidden;

import com.process.clash.application.common.exception.exception.ForbiddenException;
import com.process.clash.application.profile.exception.statuscode.ProfileStatusCode;

public class ProfilePrivatedException extends ForbiddenException {
    public ProfilePrivatedException() {
        super(ProfileStatusCode.PROFILE_PRIVATED);
    }
}
