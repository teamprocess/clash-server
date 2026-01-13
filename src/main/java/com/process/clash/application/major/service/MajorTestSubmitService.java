package com.process.clash.application.major.service;

import com.process.clash.application.major.data.MajorTestSubmitData;
import com.process.clash.application.major.port.in.MajorTestSubmitUseCase;
import com.process.clash.domain.common.enums.Major;
import org.springframework.stereotype.Service;

@Service
public class MajorTestSubmitService implements MajorTestSubmitUseCase {


    @Override
    public Void execute(MajorTestSubmitData.Command command) {
        return null;
    }
}
