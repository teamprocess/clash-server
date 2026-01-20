package com.process.clash.adapter.web.compete.rival.rival.dto;

import com.process.clash.application.compete.rival.rival.data.AbleRivalInfo;
import com.process.clash.application.compete.rival.rival.data.SearchRivalByKeywordData;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

public class SearchRivalByKeywordDto {

    @Schema(name = "SearchRivalByKeywordDtoResponse")

    public record Response(
            List<AbleRivalInfo> users
    ) {

        public static Response from(SearchRivalByKeywordData.Result result) {

            return new Response(
                    result.users()
            );
        }
    }
}
