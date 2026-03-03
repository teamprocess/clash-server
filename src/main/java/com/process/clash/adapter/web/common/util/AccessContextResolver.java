package com.process.clash.adapter.web.common.util;

import com.process.clash.application.common.data.AccessContext;
import jakarta.servlet.http.HttpServletRequest;

public interface AccessContextResolver {

    AccessContext extractAccessContext(HttpServletRequest request);

    String extractIpAddress(HttpServletRequest request);
}
