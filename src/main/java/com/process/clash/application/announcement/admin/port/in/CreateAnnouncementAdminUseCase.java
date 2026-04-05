package com.process.clash.application.announcement.admin.port.in;

import com.process.clash.application.announcement.admin.data.CreateAnnouncementAdminData;

public interface CreateAnnouncementAdminUseCase {
    CreateAnnouncementAdminData.Result execute(CreateAnnouncementAdminData.Command command);
}
