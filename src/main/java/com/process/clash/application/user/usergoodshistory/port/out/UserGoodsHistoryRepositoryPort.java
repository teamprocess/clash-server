package com.process.clash.application.user.usergoodshistory.port.out;

import com.process.clash.domain.user.usergoodshistory.entity.UserGoodsHistory;

public interface UserGoodsHistoryRepositoryPort {

    UserGoodsHistory save(UserGoodsHistory userGoodsHistory);
}
