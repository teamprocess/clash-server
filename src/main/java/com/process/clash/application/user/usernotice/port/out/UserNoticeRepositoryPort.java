package com.process.clash.application.user.usernotice.port.out;

import com.process.clash.domain.user.usernotice.entity.UserNotice;

import java.util.List;
import java.util.Optional;

public interface UserNoticeRepositoryPort {

    UserNotice save(UserNotice userNotice);
    void saveAll(List<UserNotice> userNotices);
    List<UserNotice> findAllByReceiverId(Long receiverId);
    Optional<UserNotice> findByIdAndReceiverId(Long id, Long receiverId);
    void deleteApplyRivalNoticeByRivalId(Long rivalId);
    void deleteAllNotices();
}
