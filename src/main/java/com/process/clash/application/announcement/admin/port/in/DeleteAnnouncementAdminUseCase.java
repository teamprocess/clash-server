package com.process.clash.application.announcement.admin.port.in;

import com.process.clash.application.announcement.admin.data.DeleteAnnouncementAdminData;

public interface DeleteAnnouncementAdminUseCase {
    void execute(DeleteAnnouncementAdminData.Command command);
}
