package com.process.clash.application.user.port.out;

public interface AuthEventRepositoryPort {
    void recordLogin(String username, String ipAddress, String device);
    void recordLogout(String username, String ipAddress, String device);
    void recordSessionExpired(String username, String ipAddress, String device);
}
