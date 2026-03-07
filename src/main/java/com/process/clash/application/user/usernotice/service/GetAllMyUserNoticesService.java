package com.process.clash.application.user.usernotice.service;

import com.process.clash.application.user.usernotice.data.GetMyUserNoticesData;
import com.process.clash.application.user.usernotice.port.in.GetAllMyUserNoticesUseCase;
import com.process.clash.application.user.usernotice.port.out.UserNoticeRepositoryPort;
import com.process.clash.domain.user.usernotice.entity.UserNotice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetAllMyUserNoticesService implements GetAllMyUserNoticesUseCase {

    private final UserNoticeRepositoryPort userNoticeRepositoryPort;

    @Override
    public GetMyUserNoticesData.Result execute(GetMyUserNoticesData.Command command) {
        List<UserNotice> notices = userNoticeRepositoryPort.findAllByReceiverIdIncludingRead(command.actor().id());
        List<GetMyUserNoticesData.NoticeItem> items = notices.stream()
                .map(GetMyUserNoticesData.NoticeItem::from)
                .toList();
        return new GetMyUserNoticesData.Result(items);
    }
}
