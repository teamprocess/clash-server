package com.process.clash.application.announcement.port.in;

import com.process.clash.application.announcement.data.GetActiveAnnouncementsData;

public interface GetActiveAnnouncementsUseCase {

    GetActiveAnnouncementsData.Result execute();
}
