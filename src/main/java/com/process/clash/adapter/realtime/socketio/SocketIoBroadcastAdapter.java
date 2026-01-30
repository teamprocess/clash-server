package com.process.clash.adapter.realtime.socketio;

import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOServer;
import com.process.clash.adapter.realtime.socketio.payload.ChangePayload;
import com.process.clash.application.realtime.data.RefetchNotice;
import com.process.clash.application.realtime.port.out.BroadcastRefetchPort;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocketIoBroadcastAdapter implements BroadcastRefetchPort {

    private static final String EVENT_CHANGE = "change";

    private final SocketIOServer socketIOServer;

    @Override
    public void broadcastRefetchToUsers(RefetchNotice notice, Collection<Long> userIds) {
        if (notice == null || userIds == null || userIds.isEmpty()) {
            return;
        }
        ChangePayload payload = ChangePayload.from(notice);
        for (Long userId : userIds) {
            if (userId == null) {
                continue;
            }
            String room = SocketIoRoom.userRoom(userId);
            BroadcastOperations operations = socketIOServer.getRoomOperations(room);
            operations.sendEvent(EVENT_CHANGE, payload);
        }
    }
}
