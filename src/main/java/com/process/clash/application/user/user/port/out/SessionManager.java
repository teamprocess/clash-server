package com.process.clash.application.user.user.port.out;

import com.process.clash.application.user.user.data.AuthPrincipal;

public interface SessionManager {
    void createSession(AuthPrincipal principal, boolean rememberMe);
    void invalidateSession();
    String getCurrentUsername();
}
