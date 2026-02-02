package com.process.clash.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "electron.auth")
public class ElectronAuthProps {
    private List<String> allowedRedirectUris;
    private String authWebUrl;
}
