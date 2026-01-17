package com.process.clash.adapter.web.shop.season.dto;

import com.process.clash.application.common.actor.Actor;
import com.process.clash.application.shop.season.data.CreateSeasonData;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class CreateSeasonDto {

    public record Request(
            @NotBlank(message = "시즌명은 필수 입력값입니다.")
            String title,
            @NotNull(message = "시작일은 필수 입력값입니다.")
            LocalDate startDate,
            @NotNull(message = "종료일은 필수 입력값입니다.")
            LocalDate endDate
    ) {
        public CreateSeasonData.Command toCommand(Actor actor) {
            return new CreateSeasonData.Command(
                    actor,
                    title,
                    startDate,
                    endDate
            );
        }
    }
}