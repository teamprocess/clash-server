package com.process.clash.adapter.web.compete.my.dto;

import com.process.clash.application.compete.my.data.CompareGitHubData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

public class CompareGitHubDto {

    @Schema(name = "CompareGitHubDtoResponse")

    public record Response(
            CompareGitHubData.OneDayStats yesterday,
            CompareGitHubData.OneDayStats today
    ) {
        public static Response from(CompareGitHubData.Result result) {
            return new Response(
                    result.yesterday(),
                    result.today()
            );
        }
    }
}