package com.process.clash.application.realtime.port.out;

import com.process.clash.application.realtime.data.RefetchNotice;
import java.util.Collection;

public interface BroadcastRefetchPort {
    void broadcastRefetchToUsers(RefetchNotice notice, Collection<Long> userIds);
}
