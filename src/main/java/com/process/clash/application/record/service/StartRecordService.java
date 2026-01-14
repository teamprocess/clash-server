package com.process.clash.application.record.service;

import com.process.clash.application.record.dto.StartRecordData;
import com.process.clash.application.record.port.in.StartRecordUseCase;
import org.springframework.stereotype.Service;

@Service
public class StartRecordService implements StartRecordUseCase {

    @Override
    public StartRecordData.Result execute(StartRecordData.Command command) {


        return null;
    }
}
