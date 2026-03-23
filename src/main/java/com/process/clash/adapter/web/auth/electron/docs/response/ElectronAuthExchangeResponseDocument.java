package com.process.clash.adapter.web.auth.electron.docs.response;

import com.process.clash.adapter.web.auth.electron.dto.ElectronAuthDto;
import com.process.clash.adapter.web.common.docs.response.SuccessResponseDocument;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Electron 로그인 코드 교환 응답")
public class ElectronAuthExchangeResponseDocument extends SuccessResponseDocument {

    @Schema(description = "교환 결과 데이터", implementation = ElectronAuthDto.ExchangeResponse.class)
    public ElectronAuthDto.ExchangeResponse data;
}
