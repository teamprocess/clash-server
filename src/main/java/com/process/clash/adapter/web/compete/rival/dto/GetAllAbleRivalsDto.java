package com.process.clash.adapter.web.compete.rival.dto;

import com.process.clash.application.compete.rival.data.GetAllAbleRivalsData;

import java.util.List;

public class GetAllAbleRivalsDto {

    public record Response(
        List<UserInfo> users
    ) {

        public static Response from(GetAllAbleRivalsData.Result result) {

            return new Response(
                    result.users().stream()
                            .map(data -> new UserInfo(
                                    data.id(),
                                    data.name(),
                                    data.githubId()
                            ))
                    .toList()
            );
        }
    }

    private record UserInfo(
            Long id,
            String name,
            String githubId
    ) {}
}
