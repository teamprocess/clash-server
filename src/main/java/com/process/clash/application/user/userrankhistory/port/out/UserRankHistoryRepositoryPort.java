package com.process.clash.application.user.userrankhistory.port.out;

import com.process.clash.domain.user.userrankhistory.entity.UserRankHistory;

import java.util.List;

public interface UserRankHistoryRepositoryPort {

    UserRankHistory save(UserRankHistory userRankHistory);
    void saveAll(List<UserRankHistory> histories);
}
