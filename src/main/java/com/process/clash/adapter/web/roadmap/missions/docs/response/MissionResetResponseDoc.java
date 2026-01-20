package com.process.clash.adapter.web.roadmap.missions.docs.response;

import com.process.clash.adapter.web.common.docs.response.SuccessResponseDoc;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "미션 초기화 응답")
public class MissionResetResponseDoc extends SuccessResponseDoc {

    @Schema(description = "데이터 (미션 초기화 시 null)")
    public Void data;
}
