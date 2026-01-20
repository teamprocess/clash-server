package com.process.clash.application.user.usercookiehistory.port.out;

import com.process.clash.domain.user.usercookiehistory.entity.UserCookieHistory;

public interface UserCookieHistoryRepositoryPort {

    UserCookieHistory save(UserCookieHistory userCookieHistory);
}
