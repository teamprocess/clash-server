package com.process.clash.application.user.userexphistory.port.out;

import com.process.clash.domain.user.userexphistory.entity.UserExpHistory;

public interface UserExpHistoryRepositoryPort {
    UserExpHistory save(UserExpHistory userExpHistory);
}
