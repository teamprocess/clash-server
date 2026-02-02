package com.process.clash.application.google.port.out;

public interface RecaptchaPort {

    boolean verifyToken(String token);
}
