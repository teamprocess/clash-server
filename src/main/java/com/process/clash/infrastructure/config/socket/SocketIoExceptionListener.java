package com.process.clash.infrastructure.config.socket;

import com.corundumstudio.socketio.listener.DefaultExceptionListener;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SocketIoExceptionListener extends DefaultExceptionListener {

    @Override
    public boolean exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
        // Upgrade 헤더가 없는 잘못된 요청(HTTP 등)이 올 때 발생하는 로그 제어
        if (e.getMessage() != null && e.getMessage().contains("not a WebSocket request")) {
            log.warn("[Socket.IO] 비정상적인 연결 시도 차단: {} (IP: {})",
                    e.getMessage(), ctx.channel().remoteAddress());
            return true; // 에러 전파를 여기서 중단하여 불필요한 스택 트레이스 방지
        }
        return super.exceptionCaught(ctx, e);
    }
}