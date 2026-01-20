package com.process.clash.adapter.web.compete.rival.rival.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import com.process.clash.adapter.web.compete.rival.rival.dto.SearchRivalByKeywordDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "라이벌 검색 결과 응답")
public class SearchRivalByKeywordResponseDoc extends SuccessResponseDoc {

    @Schema(description = "검색된 유저 목록", implementation = SearchRivalByKeywordDto.Response.class)
    public SearchRivalByKeywordDto.Response data;
}
