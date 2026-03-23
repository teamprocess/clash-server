package com.process.clash.adapter.web.auth.electron.docs.response;

import com.process.clash.adapter.web.auth.electron.dto.ElectronAuthDto;
import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Electron 로그인 시작 응답")
public class ElectronAuthStartResponseDocument extends SuccessResponseDocument {

    @Schema(description = "로그인 시작 데이터", implementation = ElectronAuthDto.StartResponse.class)
    public ElectronAuthDto.StartResponse data;
}
