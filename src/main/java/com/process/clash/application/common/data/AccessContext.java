package com.process.clash.application.common.data;

public record AccessContext(String ipAddress, String userAgent) {

    public static AccessContext of(String ipAddress, String userAgent) {
        return new AccessContext(ipAddress, userAgent);
    }

    public static AccessContext unknown() {
        return new AccessContext("UNKNOWN", "UNKNOWN");
    }

}
