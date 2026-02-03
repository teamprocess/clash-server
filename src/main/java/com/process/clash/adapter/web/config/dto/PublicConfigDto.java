package com.process.clash.adapter.web.config.dto;

public class PublicConfigDto {

	public record RecaptchaConfig(String siteKey) {}

	public record Response(RecaptchaConfig recaptcha) {}
}
