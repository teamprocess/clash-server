package com.process.clash.adapter.web.record.v2.dto;

import com.process.clash.application.record.v2.data.GetCurrentRecordV2Data;

public class GetCurrentRecordV2Dto {

    public static RecordSessionV2Dto.Session from(GetCurrentRecordV2Data.Result result) {
        return RecordSessionV2Dto.Session.from(result.session());
    }
}
