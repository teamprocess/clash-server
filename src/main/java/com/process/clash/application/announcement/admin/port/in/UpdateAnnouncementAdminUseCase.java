package com.process.clash.application.announcement.admin.port.in;

import com.process.clash.application.announcement.admin.data.UpdateAnnouncementAdminData;

public interface UpdateAnnouncementAdminUseCase {
    UpdateAnnouncementAdminData.Result execute(UpdateAnnouncementAdminData.Command command);
}
