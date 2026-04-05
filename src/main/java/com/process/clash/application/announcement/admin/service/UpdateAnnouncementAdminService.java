package com.process.clash.application.announcement.admin.service;

import com.process.clash.application.announcement.admin.data.UpdateAnnouncementAdminData;
import com.process.clash.application.announcement.admin.port.in.UpdateAnnouncementAdminUseCase;
import com.process.clash.application.announcement.exception.exception.notfound.AnnouncementNotFoundException;
import com.process.clash.application.announcement.port.out.AnnouncementRepositoryPort;
import com.process.clash.application.common.policy.CheckAdminPolicy;
import com.process.clash.domain.announcement.entity.Announcement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateAnnouncementAdminService implements UpdateAnnouncementAdminUseCase {

    private final AnnouncementRepositoryPort announcementRepositoryPort;
    private final CheckAdminPolicy checkAdminPolicy;

    @Override
    public UpdateAnnouncementAdminData.Result execute(UpdateAnnouncementAdminData.Command command) {
        checkAdminPolicy.check(command.actor());

        Announcement existing = announcementRepositoryPort.findById(command.id())
                .orElseThrow(AnnouncementNotFoundException::new);

        Announcement updated = new Announcement(
                existing.id(),
                existing.createdAt(),
                null,
                command.title(),
                command.author(),
                existing.userId(),
                command.content(),
                command.startedAt(),
                command.endedAt()
        );

        Announcement saved = announcementRepositoryPort.save(updated);
        return UpdateAnnouncementAdminData.Result.from(saved);
    }
}
