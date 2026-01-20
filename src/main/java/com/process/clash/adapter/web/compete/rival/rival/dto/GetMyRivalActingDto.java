package com.process.clash.adapter.web.compete.rival.rival.dto;

import com.process.clash.application.compete.rival.rival.data.GetMyRivalActingData;
import com.process.clash.domain.rival.rival.enums.RivalCurrentStatus;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

public class GetMyRivalActingDto {

    @Schema(name = "GetMyRivalActingDtoResponse")

    public record Response(
            List<MyRival> myRivals
    ) {

        public static Response from(GetMyRivalActingData.Result result) {
            return new Response(
                result.myRivals().stream()
                    .map(data -> new MyRival(
                        data.name(),
                        data.username(),
                        data.profileImage(),
                        data.activeTime(),
                        data.usingApp(),
                        data.status()
                    ))
                .toList()
            );
        }
    }

    private record MyRival(
            String name,
            String username,
            String profileImage,
            Long activeTime,
            String usingApp,
            RivalCurrentStatus status
    ) {}
}
