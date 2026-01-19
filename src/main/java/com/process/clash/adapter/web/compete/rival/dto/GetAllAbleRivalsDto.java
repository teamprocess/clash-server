package com.process.clash.adapter.web.compete.rival.dto;

import com.process.clash.application.compete.rival.data.AbleRivalInfo;
import com.process.clash.application.compete.rival.data.GetAllAbleRivalsData;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

public class GetAllAbleRivalsDto {

    @Schema(name = "GetAllAbleRivalsDtoResponse")

    public record Response(
        List<AbleRivalInfo> users
    ) {

        public static Response from(GetAllAbleRivalsData.Result result) {

            return new Response(
                    result.users()
            );
        }
    }
}
