package com.process.clash.adapter.web.compete.rival.dto;

import com.process.clash.application.compete.rival.data.GetMyRivalActingData;
import com.process.clash.domain.rival.enums.RivalStatus;

import java.util.List;

public class GetMyRivalActingDto {

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
            String activeTime,
            String usingApp,
            RivalStatus status
    ) {}
}
