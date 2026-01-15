package com.process.clash.adapter.web.compete.rival.dto;

import com.process.clash.application.compete.rival.data.SearchRivalByKeywordData;

import java.util.List;

public class SearchRivalByKeywordDto {

    public record Response(
            List<User> users
    ) {

        public static Response from(SearchRivalByKeywordData.Result result) {

            return new Response(
                    result.users().stream()
                            .map(data -> new User(
                                    data.id(),
                                    data.name(),
                                    data.githubId()
                            ))
                            .toList()
            );
        }
    }

    private record User(
            Long id,
            String name,
            String githubId
    ) {}
}
