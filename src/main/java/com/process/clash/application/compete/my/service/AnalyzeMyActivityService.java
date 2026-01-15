package com.process.clash.application.compete.my.service;

import com.process.clash.application.compete.my.data.AnalyzeMyActivityData;
import com.process.clash.application.compete.my.port.in.AnalyzeMyActivityUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyzeMyActivityService implements AnalyzeMyActivityUseCase {

    @Override
    public AnalyzeMyActivityData.Result execute(AnalyzeMyActivityData.Command command) {

        return null;
    }
}
