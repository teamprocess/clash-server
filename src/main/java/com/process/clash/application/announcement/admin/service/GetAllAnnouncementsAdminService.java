package com.process.clash.application.announcement.admin.service;

import com.process.clash.application.announcement.admin.data.GetAllAnnouncementsAdminData;
import com.process.clash.application.announcement.admin.port.in.GetAllAnnouncementsAdminUseCase;
import com.process.clash.application.announcement.port.out.AnnouncementRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetAllAnnouncementsAdminService implements GetAllAnnouncementsAdminUseCase {

    private final AnnouncementRepositoryPort announcementRepositoryPort;

    @Override
    public GetAllAnnouncementsAdminData.Result execute() {
        List<GetAllAnnouncementsAdminData.AnnouncementItem> items = announcementRepositoryPort.findAll()
                .stream()
                .map(GetAllAnnouncementsAdminData.AnnouncementItem::from)
                .toList();
        return new GetAllAnnouncementsAdminData.Result(items);
    }
}
