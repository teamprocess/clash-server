package com.process.clash.adapter.realtime.socketio;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketIoConnectionHandler {

    private final SocketIOServer socketIOServer;
    private final SocketIoAuthService socketIoAuthService;
    private static final String CONNECT_INIT = "connectInit";
    private static final String USER_ID_KEY = "userId";

    @PostConstruct
    public void registerListeners() {
        socketIOServer.addConnectListener(this::onConnect);
        socketIOServer.addDisconnectListener(this::onDisconnect);
    }

    private void onConnect(SocketIOClient client) {
        if (Boolean.TRUE.equals(client.get(CONNECT_INIT))) {
            return;
        }
        client.set(CONNECT_INIT, true);

        Optional<Long> userId = socketIoAuthService.resolveUserId(client.getHandshakeData());
        if (userId.isEmpty()) {
            client.disconnect();
            return;
        }
        Long id = userId.get();
        client.set(USER_ID_KEY, id);
        client.joinRoom(SocketIoRoom.userRoom(id));
        log.info("Socket.IO client connected: {}", client.getSessionId());
    }

    private void onDisconnect(SocketIOClient client) {
        // Room membership is handled by Socket.IO; no-op.
        log.info("Socket.IO client disconnected: {}", client.getSessionId());
    }
}
