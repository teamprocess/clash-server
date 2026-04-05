package com.process.clash.application.announcement.admin.port.in;

import com.process.clash.application.announcement.admin.data.GetAllAnnouncementsAdminData;

public interface GetAllAnnouncementsAdminUseCase {
    GetAllAnnouncementsAdminData.Result execute();
}
