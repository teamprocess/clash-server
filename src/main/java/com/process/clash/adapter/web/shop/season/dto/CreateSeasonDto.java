package com.process.clash.adapter.web.shop.season.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class CreateSeasonDto {

    public record Request(
            @NotBlank
            String title,
            @NotNull
            LocalDate startDate,
            @NotNull
            LocalDate endDate
    ) {}
}
