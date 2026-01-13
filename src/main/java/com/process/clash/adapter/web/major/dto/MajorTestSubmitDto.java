package com.process.clash.adapter.web.major.dto;

import com.process.clash.domain.common.enums.Major;

public record MajorTestSubmitDto() {

    public record Request(
            Major major
    ) {}

}