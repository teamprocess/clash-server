package com.process.clash.application.common.port.out;

/**
 * DEPRECATED: Previously used to provide request-scoped IP/User-Agent.
 *
 * This interface is scheduled for removal. Prefer passing
 * {@code com.process.clash.application.common.data.AccessContext}
 * explicitly into use-case commands instead of using this provider.
 */
@Deprecated
public interface RequestContextProvider {
    String getIpAddress();
    String getUserAgent();
}
