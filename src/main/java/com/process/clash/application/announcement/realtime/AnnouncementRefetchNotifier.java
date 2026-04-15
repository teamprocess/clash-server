package com.process.clash.application.announcement.realtime;

import com.process.clash.application.realtime.data.ChangeDomain;
import com.process.clash.application.realtime.data.ChangeType;
import com.process.clash.application.realtime.data.RefetchNotice;
import com.process.clash.application.realtime.service.RefetchEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnnouncementRefetchNotifier {

    private final RefetchEventPublisher refetchEventPublisher;

    public void notifyAnnouncementChanged() {
        refetchEventPublisher.publishToAll(RefetchNotice.of(ChangeDomain.ANNOUNCEMENT, ChangeType.DATA_CHANGED));
    }
}
