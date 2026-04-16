package com.process.clash.application.announcement.admin.service;

import com.process.clash.application.announcement.admin.data.CreateAnnouncementAdminData;
import com.process.clash.application.announcement.admin.port.in.CreateAnnouncementAdminUseCase;
import com.process.clash.application.announcement.port.out.AnnouncementRepositoryPort;
import com.process.clash.application.announcement.realtime.AnnouncementRefetchNotifier;
import com.process.clash.application.common.policy.CheckAdminPolicy;
import com.process.clash.domain.announcement.entity.Announcement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateAnnouncementAdminService implements CreateAnnouncementAdminUseCase {

    private final AnnouncementRepositoryPort announcementRepositoryPort;
    private final CheckAdminPolicy checkAdminPolicy;
    private final AnnouncementRefetchNotifier announcementRefetchNotifier;

    @Override
    public CreateAnnouncementAdminData.Result execute(CreateAnnouncementAdminData.Command command) {
        checkAdminPolicy.check(command.actor());

        Announcement saved = announcementRepositoryPort.save(command.toDomain());
        announcementRefetchNotifier.notifyAnnouncementChanged();
        return CreateAnnouncementAdminData.Result.from(saved);
    }
}
