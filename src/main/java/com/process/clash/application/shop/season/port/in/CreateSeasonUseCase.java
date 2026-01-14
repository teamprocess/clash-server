package com.process.clash.application.shop.season.port.in;

import com.process.clash.application.shop.season.data.CreateSeasonData;

public interface CreateSeasonUseCase {
    void execute(CreateSeasonData.Command command);
}
