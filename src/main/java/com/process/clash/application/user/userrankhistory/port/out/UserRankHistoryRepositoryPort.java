package com.process.clash.application.user.userrankhistory.port.out;

import com.process.clash.domain.user.userrankhistory.entity.UserRankHistory;

public interface UserRankHistoryRepositoryPort {

    UserRankHistory save(UserRankHistory userRankHistory);
}
