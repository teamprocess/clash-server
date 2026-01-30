package com.process.clash.adapter.realtime.socketio;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class SocketIoRoom {

    private static final String USER_ROOM_PREFIX = "user:";

    public static String userRoom(Long userId) {
        return USER_ROOM_PREFIX + userId;
    }
}
