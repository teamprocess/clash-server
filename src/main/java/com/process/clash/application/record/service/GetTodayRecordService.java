package com.process.clash.application.record.service;

import com.process.clash.application.record.dto.GetTodayRecordData;
import com.process.clash.application.record.port.in.GetTodayRecordUseCase;
import com.process.clash.common.DateUtil;

public class GetTodayRecordService implements GetTodayRecordUseCase {

    public GetTodayRecordData.Result execute(GetTodayRecordData.Command command) {

//        User user =
        String date = DateUtil.getCurrentDate();


        return null;
    }
}
