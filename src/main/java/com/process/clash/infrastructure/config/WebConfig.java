package com.process.clash.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebConfig {

    private final static int CONNECT_TIMEOUT_MILLISECOND = 5000;
    private final static int READ_TIMEOUT_MILLISECOND = 10000;

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(CONNECT_TIMEOUT_MILLISECOND);
        factory.setReadTimeout(READ_TIMEOUT_MILLISECOND);

        return new RestTemplate(factory);
    }
}
