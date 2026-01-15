package com.process.clash.application.common.exception.exception;

import com.process.clash.application.common.exception.statuscode.CommonStatusCode;
import lombok.Getter;

@Getter
public class EndpointMovedException extends GoneException {
    private final String newEndpoint;

    public EndpointMovedException(String newEndpoint) {
        super(CommonStatusCode.ENDPOINT_MOVED);
        this.newEndpoint = newEndpoint;
    }

    public EndpointMovedException(String newEndpoint, Throwable cause) {
        super(CommonStatusCode.ENDPOINT_MOVED, cause);
        this.newEndpoint = newEndpoint;
    }
}
