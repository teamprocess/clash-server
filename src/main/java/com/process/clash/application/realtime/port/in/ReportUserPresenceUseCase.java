package com.process.clash.application.realtime.port.in;

public interface ReportUserPresenceUseCase {

    void connected(String connectionId, Long userId);

    void disconnected(String connectionId);

    void markedAway(String connectionId);

    void markedOnline(String connectionId);
}
