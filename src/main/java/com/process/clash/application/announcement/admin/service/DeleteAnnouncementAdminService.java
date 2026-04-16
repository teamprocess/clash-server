package com.process.clash.application.announcement.admin.service;

import com.process.clash.application.announcement.admin.data.DeleteAnnouncementAdminData;
import com.process.clash.application.announcement.admin.port.in.DeleteAnnouncementAdminUseCase;
import com.process.clash.application.announcement.exception.exception.notfound.AnnouncementNotFoundException;
import com.process.clash.application.announcement.port.out.AnnouncementRepositoryPort;
import com.process.clash.application.announcement.realtime.AnnouncementRefetchNotifier;
import com.process.clash.application.common.policy.CheckAdminPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteAnnouncementAdminService implements DeleteAnnouncementAdminUseCase {

    private final AnnouncementRepositoryPort announcementRepositoryPort;
    private final CheckAdminPolicy checkAdminPolicy;
    private final AnnouncementRefetchNotifier announcementRefetchNotifier;

    @Override
    public void execute(DeleteAnnouncementAdminData.Command command) {
        checkAdminPolicy.check(command.actor());

        if (announcementRepositoryPort.findById(command.id()).isEmpty()) {
            throw new AnnouncementNotFoundException();
        }

        announcementRepositoryPort.deleteById(command.id());
        announcementRefetchNotifier.notifyAnnouncementChanged();
    }
}
