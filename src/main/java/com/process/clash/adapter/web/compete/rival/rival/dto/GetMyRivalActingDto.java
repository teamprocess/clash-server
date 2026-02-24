package com.process.clash.adapter.web.compete.rival.rival.dto;

import com.process.clash.application.compete.rival.rival.data.GetMyRivalActingData;
import com.process.clash.application.realtime.data.UserActivityStatus;

import java.util.List;

public class GetMyRivalActingDto {

    public record Response(
            List<MyRival> myRivals
    ) {

        public static Response from(GetMyRivalActingData.Result result) {
            return new Response(
                result.myRivals().stream()
                    .map(data -> new MyRival(
                        data.rivalId(),
                        data.id(),
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
            Long rivalId,
            Long id,
            String name,
            String username,
            String profileImage,
            Long activeTime,
            String usingApp,
            UserActivityStatus status
    ) {}
}
