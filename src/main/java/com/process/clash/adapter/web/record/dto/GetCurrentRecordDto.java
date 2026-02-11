package com.process.clash.adapter.web.record.dto;

import com.process.clash.application.record.data.GetCurrentRecordData;

public class GetCurrentRecordDto {

    public static RecordSessionDto.Session from(GetCurrentRecordData.Result result) {
        return RecordSessionDto.Session.from(result.session());
    }
}
