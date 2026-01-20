package com.process.clash.application.user.usernotice.port.out;

import com.process.clash.domain.user.usernotice.entity.UserNotice;

import java.util.List;

public interface UserNoticeRepositoryPort {

    UserNotice save(UserNotice userNotice);
    void saveAll(List<UserNotice> userNotices);
}
