package com.process.clash.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "aws.s3")
public class S3Properties {

    private String bucket;
    private String region = "ap-northeast-2";
    private String profileImagePrefix = "users/profile-images";
    private String publicBaseUrl;
    private long presignExpirationSeconds = 300;
}
