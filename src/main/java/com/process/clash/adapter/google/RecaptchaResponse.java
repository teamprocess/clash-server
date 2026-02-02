package com.process.clash.adapter.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
record RecaptchaResponse(
        boolean success,
        Double score,
        @JsonProperty("error-codes") List<String> errorCodes
) {}
