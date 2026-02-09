package com.process.clash.application.realtime.port.out;

import com.process.clash.application.realtime.data.UserActivityStatus;
import java.util.Collection;
import java.util.Map;

public interface UserPresencePort {

    UserActivityStatus getStatus(Long userId);

    Map<Long, UserActivityStatus> getStatuses(Collection<Long> userIds);
}
