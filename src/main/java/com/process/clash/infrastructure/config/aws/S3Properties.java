package com.process.clash.infrastructure.config.aws;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "aws.s3")
public class S3Properties {

    @NotBlank(message = "aws.s3.bucket은 필수값입니다.")
    private String bucket;
    private String region = "ap-northeast-2";
    private String profileImagePrefix = "users/profile-images";
    private String categoryImagePrefix = "categories/images";
    private String publicBaseUrl;
    private long presignExpirationSeconds = 300;
}
