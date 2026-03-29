package com.process.clash.application.user.usergoodshistory.port.out;

import com.process.clash.domain.user.usergoodshistory.entity.UserGoodsHistory;
import java.util.List;

public interface UserGoodsHistoryRepositoryPort {

    UserGoodsHistory save(UserGoodsHistory userGoodsHistory);

    void saveAll(List<UserGoodsHistory> userGoodsHistories);

    List<Long> findDistinctProductIdsByUserId(Long userId);
}
