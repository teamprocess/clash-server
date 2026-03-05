package com.process.clash.application.user.user.port.out;

public interface AuthEventRepositoryPort {
    void recordSignIn(String username, String ipAddress, String device);
    void recordDevSignIn(String username, String ipAddress, String device);
    void recordSignOut(String username, String ipAddress, String device);
    void recordSessionExpired(String username, String ipAddress, String device);
}
