package com.process.clash.adapter.realtime.socketio;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.process.clash.application.realtime.port.in.ReportUserPresenceUseCase;
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
    private final ReportUserPresenceUseCase reportUserPresenceUseCase;

    private static final String CONNECT_INIT = "connectInit";
    private static final String USER_ID_KEY = "userId";
    private static final String EVENT_PRESENCE_AWAY = "presence:away";
    private static final String EVENT_PRESENCE_ONLINE = "presence:online";

    @PostConstruct
    public void registerListeners() {
        socketIOServer.addConnectListener(this::onConnect);
        socketIOServer.addDisconnectListener(this::onDisconnect);
        socketIOServer.addEventListener(
            EVENT_PRESENCE_AWAY,
            Object.class,
            (client, data, ackSender) -> onPresenceAway(client)
        );
        socketIOServer.addEventListener(
            EVENT_PRESENCE_ONLINE,
            Object.class,
            (client, data, ackSender) -> onPresenceOnline(client)
        );
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
        reportUserPresenceUseCase.connected(connectionId(client), id);
        log.info("Socket.IO client connected: {}", client.getSessionId());
    }

    private void onDisconnect(SocketIOClient client) {
        reportUserPresenceUseCase.disconnected(connectionId(client));
        log.info("Socket.IO client disconnected: {}", client.getSessionId());
    }

    private void onPresenceAway(SocketIOClient client) {
        reportUserPresenceUseCase.markedAway(connectionId(client));
    }

    private void onPresenceOnline(SocketIOClient client) {
        reportUserPresenceUseCase.markedOnline(connectionId(client));
    }

    private String connectionId(SocketIOClient client) {
        if (client == null || client.getSessionId() == null) {
            return null;
        }
        return client.getSessionId().toString();
    }
}
