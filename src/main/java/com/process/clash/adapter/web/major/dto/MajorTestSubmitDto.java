package com.process.clash.adapter.web.major.dto;

import com.process.clash.domain.common.enums.Major;
import io.swagger.v3.oas.annotations.media.Schema;

public class MajorTestSubmitDto {

    @Schema(name = "MajorTestSubmitDtoRequest")

    public record Request(
            Major major
    ) {}

}