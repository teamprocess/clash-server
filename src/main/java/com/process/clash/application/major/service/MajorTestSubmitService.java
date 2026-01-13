package com.process.clash.application.major.service;

import com.process.clash.application.major.data.MajorTestSubmitData;
import com.process.clash.application.major.port.in.MajorTestSubmitUseCase;
import org.springframework.stereotype.Service;

@Service
public class MajorTestSubmitService implements MajorTestSubmitUseCase {


    @Override
    public void execute(MajorTestSubmitData.Command command) {
    }
}
