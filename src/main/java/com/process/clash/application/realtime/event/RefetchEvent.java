package com.process.clash.application.realtime.event;

import com.process.clash.application.realtime.data.RefetchNotice;
import java.util.Collection;

public record RefetchEvent(
    RefetchNotice notice,
    Collection<Long> userIds
) {}
