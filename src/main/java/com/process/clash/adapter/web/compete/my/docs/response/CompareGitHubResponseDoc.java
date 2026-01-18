package com.process.clash.adapter.web.compete.my.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import com.process.clash.adapter.web.compete.my.dto.CompareGitHubDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "깃허브 비교 응답")
public class CompareGitHubResponseDoc extends SuccessResponseDoc {

    @Schema(description = "비교 결과", implementation = CompareGitHubDto.Response.class)
    public CompareGitHubDto.Response data;
}
