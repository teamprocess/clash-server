package com.process.clash.adapter.realtime.socketio.payload;

import com.process.clash.application.realtime.data.RefetchNotice;

public record ChangePayload(
    String domain,
    String type,
    String occurredAt
) {

    public static ChangePayload from(RefetchNotice notice) {
        return new ChangePayload(
            notice.domain().name(),
            notice.type().name(),
            notice.occurredAt().toString()
        );
    }
}
