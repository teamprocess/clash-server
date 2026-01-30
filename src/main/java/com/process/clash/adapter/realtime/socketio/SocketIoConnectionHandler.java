package com.process.clash.adapter.realtime.socketio;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class SocketIoConnectionHandler {

    private final SocketIOServer socketIOServer;
    private final SocketIoAuthService socketIoAuthService;

    @PostConstruct
    public void registerListeners() {
        socketIOServer.addConnectListener(this::onConnect);
        socketIOServer.addDisconnectListener(this::onDisconnect);
    }

    private void onConnect(SocketIOClient client) {
        Optional<Long> userId = socketIoAuthService.resolveUserId(client.getHandshakeData());
        if (userId.isEmpty()) {
            client.disconnect();
            return;
        }
        Long id = userId.get();
        client.set("userId", id);
        client.joinRoom(SocketIoRoom.userRoom(id));
    }

    private void onDisconnect(SocketIOClient client) {
        // Room membership is handled by Socket.IO; no-op.
    }
}
