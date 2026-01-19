package com.process.clash.adapter.web.compete.rival.dto;

import com.process.clash.application.compete.rival.data.SearchRivalByKeywordData;

import java.util.List;

public class SearchRivalByKeywordDto {

    public record Response(
            List<UserInfo> userInfos
    ) {

        public static Response from(SearchRivalByKeywordData.Result result) {

            return new Response(
                    result.userInfos().stream()
                            .map(data -> new UserInfo(
                                    data.id(),
                                    data.name(),
                                    data.githubId(),
                                    data.profileImage()
                            ))
                            .toList()
            );
        }
    }

    private record UserInfo(
            Long id,
            String name,
            String githubId,
            String profileImage
    ) {}
}
